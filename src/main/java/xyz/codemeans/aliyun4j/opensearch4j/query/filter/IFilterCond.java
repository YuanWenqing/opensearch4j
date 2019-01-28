package xyz.codemeans.aliyun4j.opensearch4j.query.filter;

import xyz.codemeans.aliyun4j.opensearch4j.query.IQueryComponent;

/**
 * author: yuanwq
 * date: 2018/4/19
 */
public interface IFilterCond extends IQueryComponent {

  /**
   * 是否比逻辑运算 {@code op} 优先
   */
  boolean priorTo(LogicalOp op);

  IFilterCond and(IFilterCond right);

  IFilterCond or(IFilterCond right);

  /**
   * author: yuanwq
   * date: 2018/9/21
   */
  enum LogicalOp {
    AND,
    OR
  }
}
