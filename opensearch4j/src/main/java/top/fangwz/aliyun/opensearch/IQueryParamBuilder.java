package top.fangwz.aliyun.opensearch;

/**
 * 构造搜索请求的query参数（非query子句）
 * <p>
 * https://help.aliyun.com/document_detail/57155.html
 *
 * @author: yuanwq
 * @date: 2018/9/18
 */
public interface IQueryParamBuilder {

  /**
   * 拼接搜索请求参数
   *
   * @return {@code sb} for chain
   */
  StringBuilder appendQueryParams(StringBuilder sb);

}
