/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.util.Collections;
import java.util.Set;

/**
 * @author yuanwq
 */
public class QueryUnit implements ISearchClause, IQueryCond {
  private static final LookupTranslator SINGLE_QUOTATION_ESCAPER =
      new LookupTranslator(new String[][]{{"'", "\\'"}, {"\\", "\\\\"}});
  private static final LookupTranslator DOUBLE_QUOTATION_ESCAPER =
      new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}});

  private final String indexField;
  private final String keyword;

  private int boost = -1;
  private boolean ifPhrase = false;

  /**
   * 不指定索引的情况下，阿里云搜索会默认取default索引，若应用没有设置default索引则这种方式会报错
   * <p>
   * 参见：https://help.aliyun.com/document_detail/29191.html
   */
  public QueryUnit(String keyword) {
    this(null, keyword);
  }

  public QueryUnit(String indexField, String keyword) {
    Preconditions.checkArgument(indexField == null || StringUtils.isNotBlank(indexField),
        "blank indexField");
    Preconditions.checkArgument(StringUtils.isNotBlank(keyword), "blank keyword");
    this.indexField = indexField;
    this.keyword = keyword;
  }

  public QueryUnit(String indexField, String keyword, int boost) {
    this(indexField, keyword);
    setBoost(boost);
  }

  public QueryUnit(String indexField, String keyword, boolean ifPhrase) {
    this(indexField, keyword);
    setIfPhrase(ifPhrase);
  }

  public String getIndexField() {
    return indexField;
  }

  public String getKeyword() {
    return keyword;
  }

  public int getBoost() {
    return boost;
  }

  /**
   * [0, 99]
   */
  public QueryUnit setBoost(int boost) {
    Preconditions.checkArgument(boost >= 0 && boost <= 99, "boost in [0, 99]: " + boost);
    this.boost = boost;
    return this;
  }

  public QueryUnit setIfPhrase(boolean ifPhrase) {
    this.ifPhrase = ifPhrase;
    return this;
  }

  public boolean getIfPhrase() {
    return ifPhrase;
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    if (indexField != null) {
      sb.append(indexField).append(":");
    }
    sb.append(getQuotedKeyword());
    if (boost != -1) {
      sb.append("^").append(boost);
    }
    return sb;
  }

  private String getQuotedKeyword() {
    if (ifPhrase) {
      return String.format("\"%s\"", DOUBLE_QUOTATION_ESCAPER.translate(keyword));
    }
    return String.format("'%s'", SINGLE_QUOTATION_ESCAPER.translate(keyword));
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

  @Override
  public Set<String> getAllFields() {
    if (indexField == null) {
      return Collections.emptySet();
    }
    return Collections.singleton(indexField);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
