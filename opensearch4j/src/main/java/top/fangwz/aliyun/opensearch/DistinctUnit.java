/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

/**
 * @author yuanwq
 */
public class DistinctUnit implements IClause {

  private final String key;
  private int times = 1;
  private int count = 1;
  private boolean reserved = true;
  private boolean updateTotalHit = false;
  private IFilterCond filter;

  public DistinctUnit(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public int getTimes() {
    return times;
  }

  /** 抽取轮数 */
  public DistinctUnit setTimes(int times) {
    this.times = times;
    return this;
  }

  public int getCount() {
    return count;
  }

  /** 每轮抽取的文档数 */
  public DistinctUnit setCount(int count) {
    this.count = count;
    return this;
  }

  public boolean getReserved() {
    return reserved;
  }

  /** 是否保留抽取后剩余的文档，若false，则搜索结果的total可能不准 */
  public DistinctUnit setReserved(boolean reserved) {
    this.reserved = reserved;
    return this;
  }

  public boolean getUpdateTotalHit() {
    return updateTotalHit;
  }

  /** reserved=false && updateTotalHit=true： 搜索结果的total会被减去distinct丢弃的文档个数 */
  public DistinctUnit setUpdateTotalHit(boolean updateTotalHit) {
    this.updateTotalHit = updateTotalHit;
    return this;
  }

  public IFilterCond getFilter() {
    return filter;
  }

  public DistinctUnit setFilter(IFilterCond filter) {
    this.filter = filter;
    return this;
  }

  @Override
  public StringBuilder toClause(StringBuilder sb) {
    sb.append("dist_key:").append(key);
    sb.append(",dist_count:").append(count);
    sb.append(",dist_times:").append(times);
    sb.append(",reserved:").append(reserved);
    sb.append(",update_total_hit:").append(updateTotalHit);
    if (filter != null) {
      sb.append(",dist_filter:").append(filter.toString());
    }
    return sb;
  }

  @Override
  public String toString() {
    return toClause(new StringBuilder()).toString();
  }
}
