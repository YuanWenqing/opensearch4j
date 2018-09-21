package top.fangwz.aliyun.opensearch.component;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: yuanwq
 * @date: 2018/9/20
 */
@Data
public class RelationalFilterCond extends AbstractFilterCond {
  private final IExpression left;
  private final RelationalOp op;
  private final IExpression right;

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    left.appendSearchParams(sb);
    sb.append(op.paramValue);
    right.appendSearchParams(sb);
    return sb;
  }

  @Override
  public boolean priorTo(LogicalOp op) {
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
