/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.Preconditions;

/**
 * {@link https://help.aliyun.com/document_detail/29156.html}
 * 
 * @author yuanwq
 */
public class ConfigClause implements ISearchClause {
  private int start = 0;
  private int hit = 10;
  private String format = "json";
  private int rerankSize = 200;

  public int getStart() {
    return start;
  }

  /** [0, 5000] */
  public ConfigClause setStart(int start) {
    Preconditions.checkArgument(start >= 0 && start <= 5000, "start in [0, 5000]: " + start);
    this.start = start;
    return this;
  }

  public int getHit() {
    return hit;
  }

  /** [0, 500] */
  public ConfigClause setHit(int hit) {
    Preconditions.checkArgument(hit >= 0 && hit <= 500, "hit in [0, 500]: " + hit);
    this.hit = hit;
    return this;
  }

  public String getFormat() {
    return format;
  }

  public ConfigClause setFormat(String format) {
    this.format = format;
    return this;
  }

  public int getRerankSize() {
    return rerankSize;
  }

  /** [0, 2000] */
  public ConfigClause setRerankSize(int rerankSize) {
    Preconditions.checkArgument(rerankSize >= 0 && rerankSize <= 2000,
        "rerank_size in [0, 2000]: " + rerankSize);
    this.rerankSize = rerankSize;
    return this;
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("config=");
    sb.append("start:").append(start);
    sb.append(",hit:").append(hit);
    sb.append(",format:").append(format);
    sb.append(",rerank_size:").append(rerankSize);
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

}
