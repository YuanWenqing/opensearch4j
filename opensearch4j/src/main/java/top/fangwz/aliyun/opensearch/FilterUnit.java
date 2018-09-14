/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import java.util.Collections;
import java.util.Set;

/**
 * @author yuanwq
 */
public class FilterUnit implements IFilterCond {
  static enum FilterOp {
    eq("="), lt("<"), lte("<="), gt(">"), gte(">="), ne("!=");

    private String op;

    FilterOp(String op) {
      this.op = op;
    }

    public String getOp() {
      return op;
    }

    public void setOp(String op) {
      this.op = op;
    }
  }

  private final FilterOp filterOp;
  private final String field;
  private final String value;

  FilterUnit(String field, FilterOp filterOp, Object value) {
    this.field = field;
    this.filterOp = filterOp;
    this.value = toFilterValue(value);
  }

  private String toFilterValue(Object value) {
    if (value instanceof Number) {
      return String.valueOf(value);
    }
    return "\"" + String.valueOf(value) + "\"";
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append(field).append(filterOp.getOp()).append(value);
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }

  @Override
  public Set<String> getAllFields() {
    return Collections.singleton(field);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public static FilterUnit eq(String field, Object value) {
    return new FilterUnit(field, FilterOp.eq, value);
  }

  public static FilterUnit ne(String field, Object value) {
    return new FilterUnit(field, FilterOp.ne, value);
  }

  public static FilterUnit gte(String field, Object value) {
    return new FilterUnit(field, FilterOp.gte, value);
  }

  public static FilterUnit lte(String field, Object value) {
    return new FilterUnit(field, FilterOp.lte, value);
  }

  public static FilterUnit lt(String field, Object value) {
    return new FilterUnit(field, FilterOp.lt, value);
  }

  public static FilterUnit gt(String field, Object value) {
    return new FilterUnit(field, FilterOp.gt, value);
  }

}
