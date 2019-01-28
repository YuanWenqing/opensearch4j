/**
 * author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch.query.aggregate;

import com.google.common.collect.Maps;
import top.fangwz.aliyun.opensearch.query.ISearchClause;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * author yuanwq
 */
public class AggregateClause implements ISearchClause {
  private final Map<String, AggregateUnit> aggregates = Maps.newLinkedHashMap();

  /**
   * groupKey相同的aggregate会覆盖之前的设置
   */
  public AggregateClause add(AggregateUnit aggregate) {
    aggregates.put(aggregate.getGroupKey(), aggregate);
    return this;
  }

  public Collection<AggregateUnit> getAggregates() {
    return Collections.unmodifiableCollection(aggregates.values());
  }

  public boolean isEmpty() {
    return aggregates.isEmpty();
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("aggregate=");
    if (isEmpty()) return sb;
    boolean first = true;
    for (AggregateUnit aggregate : aggregates.values()) {
      if (first) {
        first = false;
      } else {
        sb.append(";");
      }
      aggregate.appendQueryParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }
}
