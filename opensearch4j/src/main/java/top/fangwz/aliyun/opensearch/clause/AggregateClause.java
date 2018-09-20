/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch.clause;

import com.google.common.collect.Lists;
import top.fangwz.aliyun.opensearch.ISearchClause;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author yuanwq
 */
public class AggregateClause implements ISearchClause {
  private final List<AggregateUnit> aggregates = Lists.newArrayList();

  public AggregateClause add(AggregateUnit aggregate) {
    aggregates.add(aggregate);
    return this;
  }

  public Collection<AggregateUnit> getAggregates() {
    return Collections.unmodifiableCollection(aggregates);
  }

  public boolean isEmpty() {
    return aggregates.isEmpty();
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    if (isEmpty()) return sb;
    sb.append("aggregate=");
    boolean first = true;
    for (AggregateUnit aggregate : aggregates) {
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
