package top.fangwz.aliyun.opensearch.expr;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.fangwz.aliyun.opensearch.IFilterCond;

/**
 * @author: yuanwq
 * @date: 2018/9/20
 */
@Data
public class RelationalCond implements IFilterCond {
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
