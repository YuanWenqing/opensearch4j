/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.CloudsearchSuggest;
import com.aliyun.opensearch.object.KeyTypeEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.query.ConfigClause;
import top.fangwz.aliyun.opensearch.query.KvPairsClause;
import top.fangwz.aliyun.opensearch.query.SortClause;
import top.fangwz.aliyun.opensearch.query.aggregate.AggregateClause;
import top.fangwz.aliyun.opensearch.query.aggregate.AggregateUnit;
import top.fangwz.aliyun.opensearch.query.distinct.DistinctClause;
import top.fangwz.aliyun.opensearch.query.distinct.DistinctUnit;
import top.fangwz.aliyun.opensearch.query.filter.FilterClause;
import top.fangwz.aliyun.opensearch.query.query.QueryClause;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * @author yuanwq
 */
@Data
public class AliSearch {
  private final CloudsearchClient searchClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public AliSearch(CloudsearchClient searcherClient) {
    this.searchClient = searcherClient;
  }

  public AliSearch(String accessKey, String accessSecret, String host) {
    checkArgument(StringUtils.isNotBlank(accessKey), "accessKey must not blank");
    checkArgument(StringUtils.isNotBlank(accessSecret), "accessSecret must not blank");
    checkArgument(StringUtils.isNotBlank(host), "host must not blank");
    Map<String, Object> opts = Maps.newHashMap();
    opts.put("gzip", true);
    try {
      this.searchClient =
          new CloudsearchClient(accessKey, accessSecret, host, opts, KeyTypeEnum.ALIYUN);
    } catch (UnknownHostException e) {
      // actually already handled by checkArgument
      throw new RuntimeException(e);
    }
  }

  public <T> SearchResp<T> search(SearchReq req, ItemParser<T> parser) throws IOException {
    CloudsearchSearch search = buildSearch(req);
    String respText = search.search();
    JsonNode root = objectMapper.readTree(respText);
    SearchResp<T> resp = new SearchResp<>();
    resp.setRawResponse(respText);
    resp.setDebugInfo(search.getDebugInfo());
    resp.setQuery(req.getQuery().appendQueryParams(new StringBuilder()).toString());
    resp.setStatus(SearchResp.Status.valueOf(root.get("status").asText()));
    resp.setRequestId(root.get("request_id").asText());
    resp.addErrors(parseErrors(root.get("errors")));
    SearchResp.Result<T> result = new SearchResp.Result<>();
    resp.setResult(result);
    JsonNode resultNode = root.get("result");
    result.addComputeCosts(parseComputeCosts(resultNode.get("compute_cost")));
    ArrayNode items = (ArrayNode) resultNode.get("items");
    if (items != null) {
      for (JsonNode node : items) {
        result.addItem(parser.parse(node));
      }
    }
    result.setTotal(resultNode.get("total").asInt());
    if (req.getQuery().getAggregate() != null) {
      ArrayNode facets = (ArrayNode) resultNode.get("facet");
      if (facets != null) {
        for (JsonNode facetNode : facets) {
          result.addFacet(parseFacet(facetNode));
        }
      }
    }
    return resp;
  }

  private Facet parseFacet(JsonNode facetNode) {
    Facet facet = new Facet(facetNode.get("key").asText());
    for (JsonNode node : facetNode.get("items")) {
      String itemValue = node.get("value").asText();
      Facet.FacetItem item = new Facet.FacetItem(itemValue);
      facet.putItem(itemValue, item);
      Iterator<String> iterator = node.fieldNames();
      while (iterator.hasNext()) {
        String aggFuncName = iterator.next();
        if ("value".equals(aggFuncName)) {
          continue;
        }
        item.putAggregateValue(aggFuncName, node.get(aggFuncName).numberValue());
      }
    }
    return facet;
  }

  private List<Error> parseErrors(JsonNode errorNode) {
    if (errorNode == null) {
      return Collections.emptyList();
    }
    List<Error> errors = Lists.newArrayList();
    for (JsonNode error : errorNode) {
      errors.add(new Error(error.get("code").asInt(), error.get("message").asText()));
    }
    return errors;
  }

  private List<SearchResp.ComputeCost> parseComputeCosts(JsonNode costNode) {
    if (costNode == null) {
      return Collections.emptyList();
    }
    List<SearchResp.ComputeCost> costs = Lists.newArrayList();
    for (JsonNode node : costNode) {
      costs.add(new SearchResp.ComputeCost(node.get("index_name").asText(),
          node.get("value").asDouble()));
    }
    return costs;
  }

  private CloudsearchSearch buildSearch(SearchReq req) {
    CloudsearchSearch search = new CloudsearchSearch(searchClient);
    search.addIndex(req.getAppName());
    if (StringUtils.isNotBlank(req.getSecondRankFormula())) {
      search.setFormulaName(req.getSecondRankFormula());
    }
    if (StringUtils.isNotBlank(req.getFirstRankFormula())) {
      search.setFirstFormulaName(req.getFirstRankFormula());
    }
    if (!req.getQps().isEmpty()) {
      search.addQpNames(Lists.newArrayList(req.getQps()));
    }
    if (!req.getFetchFields().isEmpty()) {
      search.addFetchFields(Lists.newArrayList(req.getFetchFields()));
    }
    setQuery(search, req.getQuery().getQuery());
    setConfig(search, req.getQuery().getConfig());
    setFilter(search, req.getQuery().getFilter());
    setSort(search, req.getQuery().getSort());
    setAggregate(search, req.getQuery().getAggregate());
    setDistinct(search, req.getQuery().getDistinct());
    setKvPairs(search, req.getQuery().getKvpairs());
    return search;
  }

  private void setQuery(CloudsearchSearch search, QueryClause queryClause) {
    if (queryClause == null || queryClause.getCond() == null) {
      search.setQueryString(StringUtils.EMPTY);
    } else {
      search
          .setQueryString(queryClause.getCond().appendQueryParams(new StringBuilder()).toString());
    }
  }

  private void setConfig(CloudsearchSearch search, ConfigClause configClause) {
    search.setFormat(ConfigClause.Format.JSON.getFormatName());
    if (configClause == null) {
      return;
    }
    search.setStartHit(configClause.getStart());
    search.setHits(configClause.getHit());
    search.setRerankSize(configClause.getRerankSize());
  }

  private void setFilter(CloudsearchSearch search, FilterClause filterClause) {
    if (filterClause == null || filterClause.isEmpty()) {
      return;
    }
    search.addFilter(filterClause.getCond().appendQueryParams(new StringBuilder()).toString());
  }

  private void setSort(CloudsearchSearch search, SortClause sortClause) {
    if (sortClause == null || sortClause.isEmpty()) {
      return;
    }
    for (Map.Entry<String, SortClause.Direction> sort : sortClause.getSorts()) {
      search.addSort(sort.getKey(), sort.getValue().getDirectionChar());
    }
  }

  private void setDistinct(CloudsearchSearch search, DistinctClause distinctClause) {
    if (distinctClause == null) {
      return;
    }
    for (DistinctUnit distinctUnit : distinctClause.getDistincts()) {
      String filter = null;
      if (distinctUnit.getFilter() != null) {
        filter = distinctUnit.getFilter().appendQueryParams(new StringBuilder()).toString();
      }
      search.addDistinct(distinctUnit.getKey(), distinctUnit.getCount(), distinctUnit.getTimes(),
          String.valueOf(distinctUnit.isReserved()), filter,
          String.valueOf(distinctUnit.isUpdateTotalHit()));
    }
  }

  private void setAggregate(CloudsearchSearch search, AggregateClause aggregateClause) {
    if (aggregateClause == null || aggregateClause.isEmpty()) {
      return;
    }
    for (AggregateUnit aggregate : aggregateClause.getAggregates()) {
      String maxGroup = null;
      if (aggregate.getMaxGroup() > 0) {
        maxGroup = String.valueOf(aggregate.getMaxGroup());
      }
      String filter = null;
      if (aggregate.getFilter() != null) {
        filter = aggregate.getFilter().appendQueryParams(new StringBuilder()).toString();
      }
      String samplerThreshold = null;
      if (aggregate.getSamplerThreshold() > 0) {
        samplerThreshold = String.valueOf(aggregate.getSamplerThreshold());
      }
      String samplerStep = null;
      if (aggregate.getSamplerStep() > 0) {
        samplerStep = String.valueOf(aggregate.getSamplerStep());
      }
      search.addAggregate(aggregate.getGroupKey(), aggregate.getJoinedFunction(),
          aggregate.getJoinedRange(), maxGroup, filter, samplerThreshold, samplerStep);
    }
  }

  private void setKvPairs(CloudsearchSearch search, KvPairsClause kvPairsClause) {
    if (kvPairsClause == null || kvPairsClause.isEmpty()) {
      return;
    }
    search.setPair(kvPairsClause.pairString());
  }

  public SuggestResp suggest(SuggestReq req) throws IOException {
    CloudsearchSuggest suggest =
        new CloudsearchSuggest(req.getAppName(), req.getSuggestName(), searchClient);
    suggest.setHit(req.getHit());
    suggest.setQuery(req.getQuery());
    String respText = suggest.search();
    JsonNode root = objectMapper.readTree(respText);
    SuggestResp resp = new SuggestResp();
    resp.setRawResponse(respText);
    resp.setDebugInfo(suggest.getDebugInfo());
    resp.addErrors(parseErrors(root.get("errors")));
    if (root.has("suggestions")) {
      for (JsonNode node : root.get("suggestions")) {
        resp.addSuggestion(node.get("suggestion").asText());
      }
    }
    return resp;
  }
}
