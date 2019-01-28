package top.fangwz.aliyun.opensearch.query;

import lombok.Data;
import top.fangwz.aliyun.opensearch.query.aggregate.AggregateClause;
import top.fangwz.aliyun.opensearch.query.distinct.DistinctClause;
import top.fangwz.aliyun.opensearch.query.filter.FilterClause;
import top.fangwz.aliyun.opensearch.query.query.QueryClause;

/**
 * 搜索参数的抽象类：https://help.aliyun.com/document_detail/57155.html
 *
 * author: yuanwq
 * date: 2018/9/22
 */
@Data
public class Query implements IQueryComponent {
  private static final QueryClause EMPTY_QUERY_CLAUSE = new QueryClause();

  /**
   * query子句
   */
  private QueryClause query;
  /**
   * config子句
   */
  private ConfigClause config;
  /**
   * filter子句
   */
  private FilterClause filter;
  /**
   * sort子句
   */
  private SortClause sort;
  /**
   * aggregate子句
   */
  private AggregateClause aggregate;
  /**
   * distinct子句
   */
  private DistinctClause distinct;
  /**
   * kvpair子句
   */
  private KvPairsClause kvpairs;

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    if (query == null) {
      EMPTY_QUERY_CLAUSE.appendQueryParams(sb);
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

}
