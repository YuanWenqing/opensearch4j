/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * @author yuanwq
 */
public class SortClause implements IClause {
  public static final String ASC = "+";
  public static final String DESC = "-";
  public static final String DEFAULT_SORT_FIELD = "RANK";

  private final Map<String, String> sorts = Maps.newLinkedHashMap();

  /**
   * @param field
   * @param direction +: asc, -: desc
   * @return
   */
  public SortClause add(String field, String direction) {
    sorts.put(field, direction);
    return this;
  }

  public SortClause asc(String field) {
    return add(field, ASC);
  }

  public SortClause desc(String field) {
    return add(field, DESC);
  }

  public String getSortText() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, String> entry : sorts.entrySet()) {
      if (first) {
        first = false;
      } else {
        sb.append(";");
      }
      sb.append(entry.getValue()).append(entry.getKey());
    }
    return sb.toString();
  }

  public boolean isEmpty() {
    return sorts.isEmpty();
  }

  public Collection<Map.Entry<String, String>> getSorts() {
    return sorts.entrySet();
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append("sort=").append(getSortText());
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }

}
