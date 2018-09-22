/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @author yuanwq
 */
@Data
public class SuggestResp {
  private String requestId;
  /**
   * 引擎查询耗时，单位为秒
   */
  private double searchTime;
  private final List<String> suggestions = Lists.newArrayList();
  private final List<Error> errors = Lists.newArrayList();
  private String debugInfo;

  private String rawResponse;

  public void addSuggestion(String suggestion) {
    this.suggestions.add(suggestion);
  }

  public void addSuggestions(Collection<String> suggestions) {
    this.suggestions.addAll(suggestions);
  }

  public boolean isError() {
    return errors.isEmpty();
  }

  public void addErrors(Collection<Error> errors) {
    this.errors.addAll(errors);
  }

  public Error findError(int code) {
    for (Error error : errors) {
      if (error.getCode() == code) {
        return error;
      }
    }
    return null;
  }

}
