package top.fangwz.aliyun.opensearch.component;

import top.fangwz.aliyun.opensearch.IQueryCond;

/**
 * @author: yuanwq
 * @date: 2018/9/21
 */
abstract class AbstractQueryCond implements IQueryCond {
  @Override
  public IQueryCond and(IQueryCond right) {
    return new LogicalQueryCond(this, LogicalOp.AND, right);
  }

  @Override
  public IQueryCond or(IQueryCond right) {
    return new LogicalQueryCond(this, LogicalOp.OR, right);
  }

  @Override
  public IQueryCond andnot(IQueryCond right) {
    return new LogicalQueryCond(this, LogicalOp.ANDNOT, right);
  }

  @Override
  public IQueryCond rank(IQueryCond right) {
    return new LogicalQueryCond(this, LogicalOp.RANK, right);
  }
}
