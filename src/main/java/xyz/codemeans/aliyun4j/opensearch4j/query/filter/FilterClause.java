/**
 * author yuanwq, date: 2017年9月12日
 */
package xyz.codemeans.aliyun4j.opensearch4j.query.filter;

import lombok.Getter;
import xyz.codemeans.aliyun4j.opensearch4j.query.ISearchClause;

/**
 * author yuanwq
 */
@Getter
public class FilterClause implements ISearchClause {
  private IFilterCond cond;

  public FilterClause setCond(IFilterCond cond) {
    this.cond = cond;
    return this;
  }

  public boolean isEmpty() {
    return cond == null;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("filter=");
    if (isEmpty()) {
      return sb;
    }
    cond.appendQueryParams(sb);
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }

}
