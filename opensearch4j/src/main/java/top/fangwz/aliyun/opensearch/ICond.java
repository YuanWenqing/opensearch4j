package top.fangwz.aliyun.opensearch;

import java.util.Set;

/**
 * @author: yuanwq
 * @date: 2018/4/19
 */
public interface ICond<T extends ICond> extends IClause {
  /**
   * 关联的所有field集合。实际上这个field有可能是个表达式(FilterUnit中的field)
   */
  Set<String> getAllFields();

  boolean isEmpty();
}
