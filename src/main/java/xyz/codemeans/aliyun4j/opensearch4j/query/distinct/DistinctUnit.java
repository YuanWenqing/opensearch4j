/**
 * author yuanwq, date: 2017年9月13日
 */
package xyz.codemeans.aliyun4j.opensearch4j.query.distinct;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import xyz.codemeans.aliyun4j.opensearch4j.query.IQueryComponent;
import xyz.codemeans.aliyun4j.opensearch4j.query.filter.IFilterCond;

import static com.google.common.base.Preconditions.*;

/**
 * author yuanwq
 */
@Getter
public class DistinctUnit implements IQueryComponent {

  private final String key;
  private int times = 1;
  private int count = 1;
  private boolean reserved = true;
  private boolean updateTotalHit = false;
  private IFilterCond filter;

  /**
   * @param key 要聚合的字段
   */
  public DistinctUnit(String key) {
    checkArgument(StringUtils.isNotBlank(key), "distKey must not blank");
    this.key = key;
  }

  /**
   * 抽取轮数
   */
  public DistinctUnit setTimes(int times) {
    this.times = times;
    return this;
  }

  /**
   * 每轮抽取的文档数
   */
  public DistinctUnit setCount(int count) {
    this.count = count;
    return this;
  }

  /**
   * 是否保留抽取后剩余的文档，若false，则搜索结果的total可能不准
   */
  public DistinctUnit setReserved(boolean reserved) {
    this.reserved = reserved;
    return this;
  }

  /**
   * {@code reserved=false && updateTotalHit=true}： 搜索结果的total会被减去distinct丢弃的文档个数
   */
  public DistinctUnit setUpdateTotalHit(boolean updateTotalHit) {
    this.updateTotalHit = updateTotalHit;
    return this;
  }

  /**
   * 过滤条件，被过滤的doc不参与distinct，只在后面的排序中，这些被过滤的doc将和被distinct出来的第一组doc一起参与排序。默认是全部参与distinct。
   */
  public DistinctUnit setFilter(IFilterCond filter) {
    this.filter = filter;
    return this;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("dist_key:").append(key);
    sb.append(",dist_count:").append(count);
    sb.append(",dist_times:").append(times);
    sb.append(",reserved:").append(reserved);
    sb.append(",update_total_hit:").append(updateTotalHit);
    if (filter != null) {
      sb.append(",dist_filter:");
      filter.appendQueryParams(sb);
    }
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }
}
