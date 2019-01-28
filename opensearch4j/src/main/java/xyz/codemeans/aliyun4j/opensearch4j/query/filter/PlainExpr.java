package xyz.codemeans.aliyun4j.opensearch4j.query.filter;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.*;

/**
 * author: yuanwq
 * date: 2018/9/21
 */
@Data
public class PlainExpr implements IExpression {
  private final String expression;

  private PlainExpr(String expression) {
    checkArgument(StringUtils.isNotBlank(expression), "expression must not blank");
    this.expression = expression;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append(expression);
    return sb;
  }
}
