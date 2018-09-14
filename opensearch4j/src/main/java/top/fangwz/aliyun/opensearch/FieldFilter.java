/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author yuanwq
 */
public class FieldFilter implements IFilterCond {

  private final FilterUnit.FilterOp filterOp;
  private final String left;
  private final String right;

  FieldFilter(String left, FilterUnit.FilterOp filterOp, String right) {
    this.left = left;
    this.filterOp = filterOp;
    this.right = right;
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append(left).append(filterOp.getOp()).append(right);
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }

  @Override
  public Set<String> getAllFields() {
    return ImmutableSet.of(left, right);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public static FieldFilter eq(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.eq, right);
  }

  public static FieldFilter ne(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.ne, right);
  }

  public static FieldFilter gte(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.gte, right);
  }

  public static FieldFilter lte(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.lte, right);
  }

  public static FieldFilter lt(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.lt, right);
  }

  public static FieldFilter gt(String left, String right) {
    return new FieldFilter(left, FilterUnit.FilterOp.gt, right);
  }

}
