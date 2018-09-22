/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.query.ConfigClause;
import top.fangwz.aliyun.opensearch.query.KvPairsClause;
import top.fangwz.aliyun.opensearch.query.Query;
import top.fangwz.aliyun.opensearch.query.SortClause;
import top.fangwz.aliyun.opensearch.query.aggregate.AggregateClause;
import top.fangwz.aliyun.opensearch.query.distinct.DistinctClause;
import top.fangwz.aliyun.opensearch.query.filter.FilterClause;
import top.fangwz.aliyun.opensearch.query.query.QueryClause;

import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * @author yuanwq
 */
@Data
public class SearchReq {

  /**
   * 搜索应用名
   */
  private final String appName;
  /**
   * query参数（非query子句）：不能为空，查询的主体，可以通过若干子句的组合来实现多样的搜索需求
   */
  private final Query query = new Query();
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
  /**
   * 可展示的字段，对查询性能影响较大，建议只获取需要的字段
   */
  private final Set<String> fetchFields = Sets.newLinkedHashSet();

  public SearchReq(String appName) {
    checkArgument(StringUtils.isNotBlank(appName), "app name must not blank");
    this.appName = appName;
  }

  public QueryClause createQuery() {
    this.query.setQuery(new QueryClause());
    return this.query.getQuery();
  }

  public ConfigClause createConfig() {
    this.query.setConfig(new ConfigClause());
    return this.query.getConfig();
  }

  public FilterClause createFilter() {
    this.query.setFilter(new FilterClause());
    return this.query.getFilter();
  }

  public SortClause createSort() {
    this.query.setSort(new SortClause());
    return this.query.getSort();
  }

  public AggregateClause createAggregate() {
    this.query.setAggregate(new AggregateClause());
    return this.query.getAggregate();
  }

  public DistinctClause createDistinct() {
    this.query.setDistinct(new DistinctClause());
    return this.query.getDistinct();
  }

  public KvPairsClause createKvpairs() {
    this.query.setKvpairs(new KvPairsClause());
    return this.query.getKvpairs();
  }

  /**
   * 查询分析规则名称
   */
  public void addQp(String qp) {
    checkArgument(StringUtils.isNotBlank(qp));
    qps.add(qp);
  }

  public void addFetchFields(Collection<String> fetchFields) {
    this.fetchFields.addAll(fetchFields);
  }

  public void addFetchField(String fetchField) {
    checkArgument(StringUtils.isNotBlank(fetchField));
    this.fetchFields.add(fetchField);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("app", appName)
        .add("query", query.appendQueryParams(new StringBuilder()).toString())
        .add("fetch", fetchFields).add("firstRank", firstRankFormula)
        .add("secondRank", secondRankFormula).add("qp", qps).toString();
  }

}
