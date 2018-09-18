package top.fangwz.aliyun.opensearch;

import com.google.common.base.Joiner;

import java.util.LinkedHashMap;

/**
 * @author: yuanwq
 * @date: 2018/6/8
 */
public class KvPairsClause extends LinkedHashMap<String, String> implements ISearchClause {
  private static final Joiner.MapJoiner joiner =
      Joiner.on(",").withKeyValueSeparator(":").useForNull("");

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append("kvpairs=").append(pairString());
    return sb;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

  public String pairString() {
    return joiner.join(this);
  }
}
