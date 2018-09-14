/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yuanwq
 */
public class AggregateUnit implements IClause {
  private final String groupKey;
  private final List<String> aggFuns = Lists.newArrayList();
  private String range;
  private IFilterCond aggFilter;
  private int aggSamplerThreshold = -1;
  private int aggSamplerStep = -1;
  private int maxGroup = -1;

  public AggregateUnit(String groupKey) {
    this.groupKey = groupKey;
  }

  public String getGroupKey() {
    return groupKey;
  }

  public String getAggFunText() {
    return StringUtils.join(aggFuns, "#");
  }

  public AggregateUnit addAggFun(String funcName, String field) {
    aggFuns.add(String.format("%s(%s)", funcName, field));
    return this;
  }

  public AggregateUnit count() {
    return addAggFun("count", "");
  }

  public AggregateUnit sum(String field) {
    return addAggFun("sum", field);
  }

  public AggregateUnit max(String field) {
    return addAggFun("max", field);
  }

  public AggregateUnit min(String field) {
    return addAggFun("min", field);
  }

  public String getRange() {
    return range;
  }

  /** 分段统计，只支持单个range。表示[min,max]及大于number2的区间情况。不支持string类型的字段分布统计。 */
  public AggregateUnit setRange(Number min, Number max) {
    this.range = String.format("%s~%s", min, max);
    return this;
  }

  public IFilterCond getAggFilter() {
    return aggFilter;
  }

  public AggregateUnit setAggFilter(IFilterCond filter) {
    this.aggFilter = filter;
    return this;
  }

  public int getAggSamplerThreshold() {
    return aggSamplerThreshold;
  }

  /** 抽样统计的阈值：该值之前的文档依次统计，该值之后的文档抽样统计 */
  public AggregateUnit setAggSamplerThreshold(int aggSamplerThreshold) {
    this.aggSamplerThreshold = aggSamplerThreshold;
    return this;
  }

  public int getAggSamplerStep() {
    return aggSamplerStep;
  }

  /** 抽样统计的步长 */
  public AggregateUnit setAggSamplerStep(int aggSamplerStep) {
    this.aggSamplerStep = aggSamplerStep;
    return this;
  }

  public int getMaxGroup() {
    return maxGroup;
  }

  /** 返回的最大组数，缺省时默认1000 */
  public AggregateUnit setMaxGroup(int maxGroup) {
    this.maxGroup = maxGroup;
    return this;
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append("group_key:").append(groupKey).append(",agg_fun:").append(getAggFunText());
    if (StringUtils.isNotBlank(range)) {
      sb.append(",range:").append(range);
    }
    if (aggFilter != null) {
      sb.append(",agg_filter:");
      aggFilter.toClause(sb);
    }
    if (aggSamplerThreshold > 0) {
      sb.append(",agg_sampler_threshold:").append(aggSamplerThreshold);
    }
    if (aggSamplerStep > 0) {
      sb.append(",agg_sampler_step:").append(aggSamplerStep);
    }
    if (maxGroup > 0) {
      sb.append(",max_group:").append(maxGroup);
    }
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }
}
