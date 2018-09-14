/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Set;

/**
 * @author yuanwq
 */
public class QueryClause implements IClause {
  private IQueryCond cond;

  public QueryClause() {
    this(null);
  }

  public QueryClause(IQueryCond cond) {
    this.cond = cond;
  }

  public QueryClause setCond(IQueryCond cond) {
    this.cond = cond;
    return this;
  }

  public IQueryCond getCond() {
    return cond;
  }

  public boolean isEmpty() {
    return cond == null || cond.isEmpty();
  }

  public Set<String> allIndexes() {
    return cond == null ? Collections.emptySet() : cond.getAllFields();
  }

  /**
   * query子句的值
   */
  public String getQueryText() {
    if (cond == null) {
      return StringUtils.EMPTY;
    }
    return cond.toClause(new StringBuilder()).toString();
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append("query=");
    if (cond == null || cond.isEmpty()) {
      // 避免空query
      sb.append("''");
    } else {
      cond.toClause(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }

}
