/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * @author yuanwq
 */
public class DistinctClause implements ISearchClause {
  private final Map<String, DistinctUnit> distincts = Maps.newLinkedHashMap();

  public DistinctClause add(DistinctUnit distinct) {
    distincts.put(distinct.getKey(), distinct);
    return this;
  }

  public Collection<DistinctUnit> getDistincts() {
    return distincts.values();
  }

  public boolean isEmpty() {
    return distincts.isEmpty();
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("distinct=");
    if (isEmpty()) return sb;
    boolean first = true;
    for (DistinctUnit distinct : distincts.values()) {
      if (first) {
        first = false;
      } else {
        sb.append(";");
      }
      distinct.appendSearchParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }
}
