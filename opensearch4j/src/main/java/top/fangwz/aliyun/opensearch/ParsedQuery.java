/**
 * @author yuanwq, date: 2017年9月14日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yuanwq
 */
public class ParsedQuery {
  private static final Joiner TERM_JOINER = Joiner.on(" ");

  private final String rawQuery;
  private Map<String, List<String>> termMap = Maps.newLinkedHashMap();
  private String errMsg;

  public ParsedQuery(String rawQuery) {
    this.rawQuery = rawQuery;
  }

  public String getRawQuery() {
    return rawQuery;
  }

  public void addTerm(String field, String term) {
    termMap.computeIfAbsent(field, f -> Lists.newLinkedList()).add(term);
  }

  public void setTerms(String field, List<String> terms) {
    termMap.put(field, terms);
  }

  public List<String> getTerms(String field) {
    return Collections.unmodifiableList(termMap.getOrDefault(field, Collections.emptyList()));
  }

  public String getJoinedTerm(String field) {
    return TERM_JOINER.join(getTerms(field));
  }

  public Map<String, List<String>> getTermMap() {
    return Collections.unmodifiableMap(termMap);
  }

  public boolean hasField(String field) {
    return termMap.containsKey(field);
  }

  public Set<String> getFields() {
    return Collections.unmodifiableSet(termMap.keySet());
  }

  public boolean isError() {
    return errMsg != null;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass()).add("raw", rawQuery).add("termMap", termMap)
        .toString();
  }
}
