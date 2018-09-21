package top.fangwz.aliyun.opensearch.component;

import lombok.Data;
import top.fangwz.aliyun.opensearch.IQueryCond;

/**
 * @author: yuanwq
 * @date: 2018/9/21
 */
@Data
public class LogicalQueryCond extends AbstractQueryCond {
  private final IQueryCond left;
  private final LogicalOp op;
  private final IQueryCond right;

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    if (left.priorTo(op)) {
      left.appendSearchParams(sb);
    } else {
      sb.append("(");
      left.appendSearchParams(sb);
      sb.append(")");
    }
    sb.append(" ").append(op.name().toUpperCase()).append(" ");
    if (right.priorTo(op)) {
      right.appendSearchParams(sb);
    } else {
      sb.append("(");
      right.appendSearchParams(sb);
      sb.append(")");
    }
    return sb;
  }

  @Override
  public boolean priorTo(LogicalOp op) {
    // 为方便理解，故只有相同的操作符可以级联
    return this.op == op;
  }

}

