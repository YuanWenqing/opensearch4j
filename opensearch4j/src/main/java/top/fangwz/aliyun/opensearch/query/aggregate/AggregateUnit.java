/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch.query.aggregate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.query.IQueryComponent;
import top.fangwz.aliyun.opensearch.query.filter.IFilterCond;

import java.util.*;

import static com.google.common.base.Preconditions.*;

/**
 * @author yuanwq
 */
@Getter
public class AggregateUnit implements IQueryComponent {
  private final String groupKey;
  private final List<AggregateDef> aggregateDefs = Lists.newArrayList();
  private final SortedSet<Number> range =
      Sets.newTreeSet(Comparator.comparingDouble(Number::doubleValue));
  private IFilterCond filter;
  private int samplerThreshold = -1;
  private int samplerStep = -1;
  private int maxGroup = -1;

  /**
   * @param groupKey 进行统计的字段名，必须配置属性字段，目前只支持对 INT，LITERAL，INT_ARRAY，LITERAL_ARRAY 字段类型做统计
   */
  public AggregateUnit(String groupKey) {
    checkArgument(StringUtils.isNotBlank(groupKey), "groupKey must not blank");
    this.groupKey = groupKey;
  }

  public AggregateUnit count() {
    this.aggregateDefs.add(new AggregateDef(AggregateFunction.COUNT, StringUtils.EMPTY));
    return this;
  }

  public AggregateUnit sum(String field) {
    checkArgument(StringUtils.isNotBlank(field), "field for sum must not blank");
    this.aggregateDefs.add(new AggregateDef(AggregateFunction.SUM, field));
    return this;
  }

  public AggregateUnit max(String field) {
    checkArgument(StringUtils.isNotBlank(field), "field for max must not blank");
    this.aggregateDefs.add(new AggregateDef(AggregateFunction.MAX, field));
    return this;
  }

  public AggregateUnit min(String field) {
    checkArgument(StringUtils.isNotBlank(field), "field for min must not blank");
    this.aggregateDefs.add(new AggregateDef(AggregateFunction.MIN, field));
    return this;
  }

  /**
   * 表示分段统计，可用于分布统计
   */
  public <T extends Number> AggregateUnit addRangeValue(T rangeValue) {
    this.range.add(rangeValue);
    return this;
  }

  public Collection<Number> getRange() {
    return Collections.unmodifiableCollection(range);
  }

  /**
   * 非必须参数，表示仅统计满足特定条件的文档
   */
  public AggregateUnit setFilter(IFilterCond filter) {
    // TODO: filter cond
    this.filter = filter;
    return this;
  }

  /**
   * 非必须参数，抽样统计的阈值：该值之前的文档依次统计，该值之后的文档抽样统计
   */
  public AggregateUnit setSamplerThreshold(int samplerThreshold) {
    this.samplerThreshold = samplerThreshold;
    return this;
  }

  /**
   * 非必须参数，抽样统计的步长。表示从 {@link #samplerThreshold} 后的文档将间隔 {@link #samplerStep} 个文档统计一次。
   */
  public AggregateUnit setSamplerStep(int samplerStep) {
    this.samplerStep = samplerStep;
    return this;
  }

  /**
   * 返回的最大组数，缺省时默认1000
   */
  public AggregateUnit setMaxGroup(int maxGroup) {
    this.maxGroup = maxGroup;
    return this;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("group_key:").append(groupKey).append(",agg_fun:");
    boolean first = true;
    for (AggregateDef aggregateDef : aggregateDefs) {
      if (first) {
        first = false;
      } else {
        sb.append("#");
      }
      aggregateDef.appendQueryParams(sb);
    }
    if (!range.isEmpty()) {
      sb.append(",range:").append(StringUtils.join(range, "~"));
    }
    if (filter != null) {
      sb.append(",agg_filter:");
      filter.appendQueryParams(sb);
    }
    if (samplerThreshold > 0) {
      sb.append(",agg_sampler_threshold:").append(samplerThreshold);
    }
    if (samplerStep > 0) {
      sb.append(",agg_sampler_step:").append(samplerStep);
    }
    if (maxGroup > 0) {
      sb.append(",max_group:").append(maxGroup);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }

  @Getter
  @AllArgsConstructor
  public static class AggregateDef implements IQueryComponent {
    private final AggregateFunction function;
    /**
     * sum、max、min的内容支持基本的算术运算；
     */
    private final String expression;

    @Override
    public StringBuilder appendQueryParams(StringBuilder sb) {
      sb.append(function.getFunctionName()).append("(").append(expression).append(")");
      return sb;
    }
  }

}
