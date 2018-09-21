package top.fangwz.aliyun.opensearch.component;

import top.fangwz.aliyun.opensearch.IFilterCond;

/**
 * @author: yuanwq
 * @date: 2018/9/21
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
