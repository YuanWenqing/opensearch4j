/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author yuanwq
 */
public class SuggestResp {
  private final List<String> suggestions = Lists.newArrayList();

  private String rawResponse;
  private String errMsg;
  private String debugInfo;

  public void addSuggestion(String suggestion) {
    this.suggestions.add(suggestion);
  }

  public void addSuggestions(Collection<String> suggestions) {
    this.suggestions.addAll(suggestions);
  }

  public List<String> getSuggestions() {
    return suggestions;
  }

  public void setRawResponse(String rawResponse) {
    this.rawResponse = rawResponse;
  }

  public String getRawResponse() {
    return rawResponse;
  }

  public boolean isError() {
    return errMsg != null;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public String getDebugInfo() {
    return debugInfo;
  }

  public void setDebugInfo(String debugInfo) {
    this.debugInfo = debugInfo;
  }

  @Override
  public String toString() {
    if (isError()) {
      return MoreObjects.toStringHelper(getClass()).add("errMsg", errMsg).add("debug", debugInfo)
          .toString();
    }
    return MoreObjects.toStringHelper(getClass()).add("suggestions", suggestions)
        .add("debug", debugInfo).toString();
  }

}
