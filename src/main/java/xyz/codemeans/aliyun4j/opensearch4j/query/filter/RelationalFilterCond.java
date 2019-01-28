package xyz.codemeans.aliyun4j.opensearch4j.query.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * author: yuanwq
 * date: 2018/9/20
 */
@Data
public class RelationalFilterCond extends AbstractFilterCond {
  private final IExpression left;
  private final RelationalOp op;
  private final IExpression right;

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    left.appendQueryParams(sb);
    sb.append(op.paramValue);
    right.appendQueryParams(sb);
    return sb;
  }

  @Override
  public boolean priorTo(IFilterCond.LogicalOp op) {
    return true;
  }

  @AllArgsConstructor
  public enum RelationalOp {
    EQ("="),
    NE("!="),
    LT("<"),
    LTE("<"),
    GT(">"),
    GTE(">=");
    private final String paramValue;
  }
}
