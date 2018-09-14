/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

/**
 * @author yuanwq
 */
public interface IClause {

  /**
   * 搜索子句的字符串形式
   * 
   * @see {@link https://help.aliyun.com/document_detail/29140.html}
   * @return {@code sb} for chain
   */
  public StringBuilder toClause(StringBuilder sb);

}
