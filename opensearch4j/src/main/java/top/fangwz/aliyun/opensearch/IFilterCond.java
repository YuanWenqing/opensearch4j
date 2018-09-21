package top.fangwz.aliyun.opensearch;

/**
 * @author: yuanwq
 * @date: 2018/4/19
 */
public interface IFilterCond extends ISearchParamsBuilder {

  /**
   * 是否比逻辑运算 {@code op} 优先
   */
  boolean priorTo(LogicalOp op);

  IFilterCond and(IFilterCond right);

  IFilterCond or(IFilterCond right);

  /**
   * @author: yuanwq
   * @date: 2018/9/21
   */
  enum LogicalOp {
    AND,
    OR
  }
}
