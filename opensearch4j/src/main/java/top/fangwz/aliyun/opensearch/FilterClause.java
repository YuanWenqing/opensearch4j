/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuanwq
 */
public class FilterClause implements IClause {
  private IFilterCond cond;

  public FilterClause() {
    this(null);
  }

  public FilterClause(IFilterCond cond) {
    this.cond = cond;
  }

  public IFilterCond getCond() {
    return cond;
  }

  public FilterClause setCond(IFilterCond cond) {
    this.cond = cond;
    return this;
  }

  public boolean isEmpty() {
    return cond == null || cond.isEmpty();
  }

  /**
   * filter子句的值
   */
  public String getFilterText() {
    if (cond == null) {
      return StringUtils.EMPTY;
    }
    return cond.toClause(new StringBuilder()).toString();
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append("filter=");
    if (isEmpty()) {
      return sb;
    }
    cond.toClause(sb);
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }

}
