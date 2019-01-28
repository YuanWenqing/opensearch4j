/**
 * author yuanwq, date: 2017年9月12日
 */
package xyz.codemeans.aliyun4j.opensearch4j.query;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * author yuanwq
 */
public class SortClause implements ISearchClause {
  public static final String DEFAULT_SORT = "RANK";

  private final Map<String, Direction> sorts = Maps.newLinkedHashMap();

  public SortClause asc(String field) {
    sorts.put(field, Direction.ASC);
    return this;
  }

  public SortClause desc(String field) {
    sorts.put(field, Direction.DESC);
    return this;
  }

  public boolean isEmpty() {
    return sorts.isEmpty();
  }

  public Collection<Map.Entry<String, Direction>> getSorts() {
    return sorts.entrySet();
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("sort=");
    if (isEmpty()) {
      return sb;
    }
    boolean first = true;
    for (Map.Entry<String, Direction> entry : sorts.entrySet()) {
      if (first) {
        first = false;
      } else {
        sb.append(";");
      }
      sb.append(entry.getValue().directionChar).append(entry.getKey());
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }

  @AllArgsConstructor
  @Getter
  public enum Direction {
    ASC("+"),
    DESC("-");

    private final String directionChar;
  }
}
