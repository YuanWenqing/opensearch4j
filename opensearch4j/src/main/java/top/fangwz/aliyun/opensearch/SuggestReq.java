/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;

/**
 * @author yuanwq
 */
public class SuggestReq {
  private final String appName;
  private final String suggestName;

  private int hit = 10;
  private String query = "";

  /**
   * @param suggestName 提示规则名称
   */
  public SuggestReq(String appName, String suggestName) {
    this.appName = appName;
    this.suggestName = suggestName;
  }

  public int getHit() {
    return hit;
  }

  public void setHit(int hit) {
    this.hit = hit;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getAppName() {
    return appName;
  }

  public String getSuggestName() {
    return suggestName;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("app", appName).add("suggest", suggestName)
        .add("query", query).add("hit", hit).toString();
  }
}
