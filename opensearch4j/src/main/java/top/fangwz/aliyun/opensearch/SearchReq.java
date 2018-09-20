/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.clause.AggregateClause;
import top.fangwz.aliyun.opensearch.clause.ConfigClause;
import top.fangwz.aliyun.opensearch.clause.SortClause;

import java.util.List;

/**
 * @author yuanwq
 */
public class SearchReq<T> implements ISearchClause {
  private final String appName;
  private final Function<JsonNode, T> translator;

  private QueryClause query;
  private ConfigClause config;
  private FilterClause filter;
  private SortClause sort;
  private AggregateClause aggregate;
  private DistinctClause distinct;
  private KvPairsClause kvpairs;

  private String formulaName;
  private String firstFormulaName;
  private List<String> qps = Lists.newArrayList();
  private List<String> fetchFields = Lists.newArrayList();

  public SearchReq(String appName, Function<JsonNode, T> translator) {
    this.appName = appName;
    this.translator = translator;
  }

  public String getAppName() {
    return appName;
  }

  public Function<JsonNode, T> getTranslator() {
    return translator;
  }

  public QueryClause createQuery() {
    this.query = new QueryClause();
    return this.query;
  }

  public QueryClause getQuery() {
    return query;
  }

  public void setQuery(QueryClause query) {
    Preconditions.checkNotNull(query);
    this.query = query;
  }

  public ConfigClause createConfig() {
    this.config = new ConfigClause();
    return this.config;
  }

  public ConfigClause getConfig() {
    return config;
  }

  public void setConfig(ConfigClause config) {
    this.config = config;
  }

  public FilterClause createFilter() {
    this.filter = new FilterClause();
    return this.filter;
  }

  public FilterClause getFilter() {
    return filter;
  }

  public void setFilter(FilterClause filter) {
    this.filter = filter;
  }

  public SortClause createSort() {
    this.sort = new SortClause();
    return this.sort;
  }

  public SortClause getSort() {
    return sort;
  }

  public void setSort(SortClause sort) {
    this.sort = sort;
  }

  public AggregateClause createAggregate() {
    this.aggregate = new AggregateClause();
    return this.aggregate;
  }

  public AggregateClause getAggregate() {
    return aggregate;
  }

  public void setAggregate(AggregateClause aggregate) {
    this.aggregate = aggregate;
  }

  public DistinctClause createDistinct() {
    this.distinct = new DistinctClause();
    return this.distinct;
  }

  public DistinctClause getDistinct() {
    return distinct;
  }

  public void setDistinct(DistinctClause distinct) {
    this.distinct = distinct;
  }

  public KvPairsClause createKvpairs() {
    this.kvpairs = new KvPairsClause();
    return this.kvpairs;
  }

  public KvPairsClause getKvpairs() {
    return kvpairs;
  }

  public void setKvpairs(KvPairsClause kvpairs) {
    this.kvpairs = kvpairs;
  }

  public String getFormulaName() {
    return formulaName;
  }

  /**
   * 精排表达式名称
   */
  public void setFormulaName(String formulaName) {
    this.formulaName = formulaName;
  }

  public String getFirstFormulaName() {
    return firstFormulaName;
  }

  /**
   * 粗排表达式名称
   */
  public void setFirstFormulaName(String firstFormulaName) {
    this.firstFormulaName = firstFormulaName;
  }

  public List<String> getQps() {
    return qps;
  }

  /**
   * 查询分析规则名称
   */
  public void addQp(String qp) {
    Preconditions.checkArgument(StringUtils.isNotBlank(qp));
    qps.add(qp);
  }

  public List<String> getFetchFields() {
    return fetchFields;
  }

  public void addFetchFields(List<String> fetchFields) {
    this.fetchFields.addAll(fetchFields);
  }

  public void addFetchField(String fetchField) {
    this.fetchFields.add(fetchField);
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    query.appendSearchParams(sb);
    if (config != null) {
      sb.append("&&");
      config.appendSearchParams(sb);
    }
    if (filter != null && !filter.isEmpty()) {
      sb.append("&&");
      filter.appendSearchParams(sb);
    }
    if (sort != null && !sort.isEmpty()) {
      sb.append("&&");
      sort.appendSearchParams(sb);
    }
    if (aggregate != null && !aggregate.isEmpty()) {
      sb.append("&&");
      aggregate.appendSearchParams(sb);
    }
    if (distinct != null && !distinct.isEmpty()) {
      sb.append("&&");
      distinct.appendSearchParams(sb);
    }
    if (kvpairs != null && !kvpairs.isEmpty()) {
      sb.append("&&");
      kvpairs.appendSearchParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("app", appName)
        .add("query", appendSearchParams(new StringBuilder()).toString()).add("fetch", fetchFields)
        .add("formulaName", formulaName).add("firstFormulaName", firstFormulaName).add("qp", qps)
        .toString();
  }

}
