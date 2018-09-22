/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.clause.*;

import java.util.Collection;
import java.util.Set;

/**
 * @author yuanwq
 */
@Data
public class SearchReq implements ISearchClause {
  private static final QueryClause EMPTY_QUERY = new QueryClause();

  private final String appName;

  private QueryClause query;
  private ConfigClause config;
  private FilterClause filter;
  private SortClause sort;
  private AggregateClause aggregate;
  private DistinctClause distinct;
  private KvPairsClause kvpairs;

  /**
   * 粗排表达式名称
   */
  private String firstRankFormula;
  /**
   * 精排表达式名称
   */
  private String secondRankFormula;
  /**
   * 查询分析规则名称
   */
  private final Set<String> qps = Sets.newLinkedHashSet();
  private final Set<String> fetchFields = Sets.newLinkedHashSet();

  public SearchReq(String appName) {
    this.appName = appName;
  }

  public QueryClause createQuery() {
    this.query = new QueryClause();
    return this.query;
  }

  public ConfigClause createConfig() {
    this.config = new ConfigClause();
    return this.config;
  }

  public FilterClause createFilter() {
    this.filter = new FilterClause();
    return this.filter;
  }

  public SortClause createSort() {
    this.sort = new SortClause();
    return this.sort;
  }

  public AggregateClause createAggregate() {
    this.aggregate = new AggregateClause();
    return this.aggregate;
  }

  public DistinctClause createDistinct() {
    this.distinct = new DistinctClause();
    return this.distinct;
  }

  public KvPairsClause createKvpairs() {
    this.kvpairs = new KvPairsClause();
    return this.kvpairs;
  }

  /**
   * 查询分析规则名称
   */
  public void addQp(String qp) {
    Preconditions.checkArgument(StringUtils.isNotBlank(qp));
    qps.add(qp);
  }

  public void addFetchFields(Collection<String> fetchFields) {
    this.fetchFields.addAll(fetchFields);
  }

  public void addFetchField(String fetchField) {
    this.fetchFields.add(fetchField);
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    if (query == null) {
      EMPTY_QUERY.appendQueryParams(sb);
    } else {
      query.appendQueryParams(sb);
    }
    if (config != null) {
      sb.append("&&");
      config.appendQueryParams(sb);
    }
    if (filter != null && !filter.isEmpty()) {
      sb.append("&&");
      filter.appendQueryParams(sb);
    }
    if (sort != null && !sort.isEmpty()) {
      sb.append("&&");
      sort.appendQueryParams(sb);
    }
    if (aggregate != null && !aggregate.isEmpty()) {
      sb.append("&&");
      aggregate.appendQueryParams(sb);
    }
    if (distinct != null && !distinct.isEmpty()) {
      sb.append("&&");
      distinct.appendQueryParams(sb);
    }
    if (kvpairs != null && !kvpairs.isEmpty()) {
      sb.append("&&");
      kvpairs.appendQueryParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("app", appName)
        .add("query", appendQueryParams(new StringBuilder()).toString()).add("fetch", fetchFields)
        .add("firstRankFormula", firstRankFormula).add("secondRankFormula", secondRankFormula)
        .add("qp", qps).toString();
  }

}
