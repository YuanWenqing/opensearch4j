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
public class AggregateClause implements ISearchClause {
  private final Map<String, AggregateUnit> aggregates = Maps.newLinkedHashMap();

  public AggregateClause add(AggregateUnit aggregate) {
    aggregates.put(aggregate.getGroupKey(), aggregate);
    return this;
  }

  public Collection<AggregateUnit> getAggregates() {
    return aggregates.values();
  }

  public boolean isEmpty() {
    return aggregates.isEmpty();
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("aggregate=");
    if (isEmpty()) return sb;
    boolean first = true;
    for (AggregateUnit aggregate : aggregates.values()) {
      if (first) {
        first = false;
      } else {
        sb.append(";");
      }
      aggregate.appendSearchParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }
}
