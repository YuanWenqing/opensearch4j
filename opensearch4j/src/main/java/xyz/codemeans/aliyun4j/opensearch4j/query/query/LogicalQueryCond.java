package xyz.codemeans.aliyun4j.opensearch4j.query.query;

import lombok.Data;

/**
 * author: yuanwq
 * date: 2018/9/21
 */
@Data
public class LogicalQueryCond extends AbstractQueryCond {
  private final IQueryCond left;
  private final LogicalOp op;
  private final IQueryCond right;

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    if (left.priorTo(op)) {
      left.appendQueryParams(sb);
    } else {
      sb.append("(");
      left.appendQueryParams(sb);
      sb.append(")");
    }
    sb.append(" ").append(op.name().toUpperCase()).append(" ");
    if (right.priorTo(op)) {
      right.appendQueryParams(sb);
    } else {
      sb.append("(");
      right.appendQueryParams(sb);
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

