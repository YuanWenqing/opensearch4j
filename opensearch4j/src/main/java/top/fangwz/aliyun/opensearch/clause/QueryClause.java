/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch.clause;

import lombok.Getter;
import top.fangwz.aliyun.opensearch.IQueryCond;
import top.fangwz.aliyun.opensearch.ISearchClause;

/**
 * @author yuanwq
 */
@Getter
public class QueryClause implements ISearchClause {
  private IQueryCond cond;

  public QueryClause setCond(IQueryCond cond) {
    this.cond = cond;
    return this;
  }

  public boolean isEmpty() {
    return cond == null || cond.isEmpty();
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("query=");
    if (cond == null || cond.isEmpty()) {
      // 避免空query
      sb.append("''");
    } else {
      cond.appendSearchParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

}
