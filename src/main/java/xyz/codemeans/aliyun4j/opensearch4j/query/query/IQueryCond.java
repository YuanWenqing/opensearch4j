package xyz.codemeans.aliyun4j.opensearch4j.query.query;

import xyz.codemeans.aliyun4j.opensearch4j.query.IQueryComponent;

/**
 * author: yuanwq
 * date: 2018/4/19
 */
public interface IQueryCond extends IQueryComponent {

  /**
   * 是否比逻辑运算 {@code op} 优先
   */
  boolean priorTo(LogicalOp op);

  IQueryCond and(IQueryCond right);

  IQueryCond or(IQueryCond right);

  IQueryCond andnot(IQueryCond right);

  IQueryCond rank(IQueryCond right);

  /**
   * author: yuanwq
   * date: 2018/9/21
   */
  enum LogicalOp {
    AND,
    OR,
    ANDNOT,
    RANK
  }
}
