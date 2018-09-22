/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.CloudsearchSuggest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.component.AggregateUnit;
import top.fangwz.aliyun.opensearch.component.DistinctUnit;
import top.fangwz.aliyun.opensearch.component.Facet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yuanwq
 */
public class AliSearch {

  private final CloudsearchClient searchClient;
  private final ObjectMapper objectMapper;

  public AliSearch(CloudsearchClient searchClient) {
    this.searchClient = searchClient;
    this.objectMapper = new ObjectMapper();
  }

  public <T> SearchResp<T> search(SearchReq<T> req) throws IOException {
    CloudsearchSearch search = buildSearch(req);
    String respText = search.search();
    JsonNode root = objectMapper.readTree(respText);
    SearchResp<T> resp = new SearchResp<>();
    resp.setRawResponse(respText);
    resp.setDebugInfo(search.getDebugInfo());
    resp.setQuery(req.appendSearchParams(new StringBuilder()).toString());
    resp.setStatus(root.get("status").asText());
    resp.setRequestId(root.get("request_id").asText());
    resp.addErrors(parseErrors(root.get("errors")));
    JsonNode result = root.get("result");
    resp.addComputeCosts(parseComputeCosts(result.get("compute_cost")));
    ArrayNode items = (ArrayNode) result.get("items");
    if (items != null) {
      for (JsonNode node : items) {
        resp.addValue(req.getTranslator().apply(node));
      }
    }
    resp.setTotal(result.get("total").asInt());
    if (req.getAggregate() != null) {
      ArrayNode facets = (ArrayNode) result.get("facet");
      if (facets != null) {
        for (JsonNode facetNode : facets) {
          resp.addFacet(new Facet(facetNode));
        }
      }
    }
    return resp;
  }

  private List<SearchResp.Error> parseErrors(JsonNode errorNode) {
    if (errorNode == null) {
      return Collections.emptyList();
    }
    List<SearchResp.Error> errors = Lists.newArrayList();
    for (JsonNode error : errorNode) {
      errors.add(new SearchResp.Error(error.get("code").asInt(), error.get("message").asText()));
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

  private CloudsearchSearch buildSearch(SearchReq<?> req) {
    CloudsearchSearch search = new CloudsearchSearch(searchClient);
    search.addIndex(req.getAppName());
    if (StringUtils.isNotBlank(req.getFormulaName())) {
      search.setFormulaName(req.getFormulaName());
    }
    if (StringUtils.isNotBlank(req.getFirstFormulaName())) {
      search.setFirstFormulaName(req.getFirstFormulaName());
    }
    if (!req.getQps().isEmpty()) {
      search.addQpNames(req.getQps());
    }
    search.setQueryString(req.getQuery().getQueryText());
    if (req.getConfig() != null) {
      search.setStartHit(req.getConfig().getStart());
      search.setHits(req.getConfig().getHit());
      search.setRerankSize(req.getConfig().getRerankSize());
    }
    search.setFormat("json");
    if (req.getFilter() != null && StringUtils.isNotBlank(req.getFilter().getFilterText())) {
      search.addFilter(req.getFilter().getFilterText());
    }
    if (req.getSort() != null && !req.getSort().isEmpty()) {
      for (Map.Entry<String, String> sort : req.getSort().getSorts()) {
        search.addSort(sort.getKey(), sort.getValue());
      }
    }
    if (!req.getFetchFields().isEmpty()) {
      search.addFetchFields(req.getFetchFields());
    }
    if (req.getAggregate() != null) {
      for (AggregateUnit aggregate : req.getAggregate().getAggregates()) {
        search
            .addAggregate(aggregate.getGroupKey(), aggregate.getAggFunText(), aggregate.getRange(),
                null, aggregate.getFilter() == null ? null : aggregate.getFilter().toString(),
                null, null);
      }
    }
    if (req.getDistinct() != null) {
      for (DistinctUnit unit : req.getDistinct().getDistincts()) {
        search.addDistinct(unit.getKey(), unit.getCount(), unit.getTimes(),
            String.valueOf(unit.getReserved()),
            unit.getFilter() == null ? null : unit.getFilter().toString(),
            String.valueOf(unit.getUpdateTotalHit()));
      }
    }
    if (req.getKvpairs() != null && !req.getKvpairs().isEmpty()) {
      search.setPair(req.getKvpairs().pairString());
    }
    return search;
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
    if (root.has("errors")) {
      JsonNode error = root.get("errors");
      resp.setErrMsg(String.valueOf(error));
      return resp;
    }
    for (JsonNode node : (ArrayNode) root.get("suggestions")) {
      resp.addSuggestion(node.get("suggestion").asText());
    }
    return resp;
  }
}
