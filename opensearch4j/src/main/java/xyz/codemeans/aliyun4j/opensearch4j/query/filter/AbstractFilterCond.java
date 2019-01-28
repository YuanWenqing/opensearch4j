package xyz.codemeans.aliyun4j.opensearch4j.query.filter;

/**
 * author: yuanwq
 * date: 2018/9/21
 */
abstract class AbstractFilterCond implements IFilterCond {

  @Override
  public IFilterCond and(IFilterCond right) {
    return new LogicalFilterCond(this, LogicalOp.AND, right);
  }

  @Override
  public IFilterCond or(IFilterCond right) {
    return new LogicalFilterCond(this, LogicalOp.OR, right);
  }
}
