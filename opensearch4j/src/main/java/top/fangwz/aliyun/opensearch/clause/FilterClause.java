/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch.clause;

import lombok.Getter;
import top.fangwz.aliyun.opensearch.IFilterCond;
import top.fangwz.aliyun.opensearch.ISearchClause;

/**
 * @author yuanwq
 */
@Getter
public class FilterClause implements ISearchClause {
  private IFilterCond cond;

  public FilterClause setCond(IFilterCond cond) {
    this.cond = cond;
    return this;
  }

  public boolean isEmpty() {
    return cond == null || cond.isEmpty();
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("filter=");
    if (isEmpty()) {
      return sb;
    }
    cond.appendSearchParams(sb);
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

}