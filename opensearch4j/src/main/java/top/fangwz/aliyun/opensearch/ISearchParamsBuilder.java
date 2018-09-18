package top.fangwz.aliyun.opensearch;

/**
 * @author: yuanwq
 * @date: 2018/9/18
 */
public interface ISearchParamsBuilder {

  /**
   * 拼接搜索请求参数
   *
   * @return {@code sb} for chain
   */
  StringBuilder appendSearchParams(StringBuilder sb);

}
