package top.fangwz.aliyun.opensearch.query.filter;

import lombok.Data;

/**
 * @author: yuanwq
 * @date: 2018/9/20
 */
@Data
public class Value implements IExpression {
  public static Value of(Object value) {
    return new Value(value);
  }

  private final Object value;

  private Value(Object value) {
    this.value = value;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    if (value instanceof Number) {
      sb.append(value);
    } else {
      sb.append("\"").append(value).append("\"");
    }
    return sb;
  }
}
