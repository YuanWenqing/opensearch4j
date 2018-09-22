/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import lombok.Data;

/**
 * @author yuanwq
 */
@Data
public class SuggestReq {
  /**
   * 搜索应用名
   */
  private final String appName;
  /**
   * 提示规则名称
   */
  private final String suggestName;
  /**
   * 搜索关键词
   */
  private String query = "";
  /**
   * 下拉提示条数，默认10，范围[1,10]
   */
  private int hit = 10;

}
