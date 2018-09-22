/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.google.common.base.Preconditions.*;

/**
 * 配置子句：https://help.aliyun.com/document_detail/29156.html
 *
 * @author yuanwq
 */
@Getter
public class ConfigClause implements ISearchClause {
  /**
   * 起始位置，默认为0，范围[0， 5000]
   * <p>
   * {@code start+hit<=5000}
   */
  private int start = 0;
  /**
   * 每页个数，默认为10，范围[0， 500]
   * <p>
   * {@code start+hit<=5000}
   */
  private int hit = 10;
  /**
   * 返回结果格式，默认为 {@link Format#JSON}
   */
  private Format format = Format.JSON;
  /**
   * 参与精排（second rank）个数，默认200，范围[0,2000]
   */
  private int rerankSize = 200;

  /**
   * {@code start+hit<=5000}
   */
  public ConfigClause setStart(int start) {
    checkArgument(start >= 0 && start <= 5000, "start must in [0, 5000]: " + start);
    checkArgument(start + hit <= 5000, "start+hit<=5000, but start=" + start + ", hit=" + hit);
    this.start = start;
    return this;
  }

  /**
   * {@code start+hit<=5000}
   */
  public ConfigClause setHit(int hit) {
    checkArgument(hit >= 0 && hit <= 500, "hit must in [0, 500]: " + hit);
    checkArgument(start + hit <= 5000, "start+hit<=5000, but start=" + start + ", hit=" + hit);
    this.hit = hit;
    return this;
  }

  public ConfigClause setFormat(Format format) {
    this.format = format;
    return this;
  }

  public ConfigClause setRerankSize(int rerankSize) {
    checkArgument(rerankSize >= 0 && rerankSize <= 2000, "rerank_size in [0, 2000]: " + rerankSize);
    this.rerankSize = rerankSize;
    return this;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    sb.append("config=");
    sb.append("start:").append(start);
    sb.append(",hit:").append(hit);
    sb.append(",format:").append(format.paramValue);
    sb.append(",rerank_size:").append(rerankSize);
    return sb;
  }

  @Override
  public String toString() {
    return appendQueryParams(new StringBuilder()).toString();
  }

  @AllArgsConstructor
  public enum Format {
    JSON("json"),
    FULL_JSON("fulljson"),
    XML("xml");

    private final String paramValue;
  }
}
