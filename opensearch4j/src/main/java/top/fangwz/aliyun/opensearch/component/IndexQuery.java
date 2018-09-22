/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch.component;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import static com.google.common.base.Preconditions.*;

/**
 * @author yuanwq
 */
@Getter
@ToString
public class IndexQuery extends AbstractQueryCond {
  private static final LookupTranslator SINGLE_QUOTATION_ESCAPER =
      new LookupTranslator(new String[][]{{"'", "\\'"}, {"\\", "\\\\"}});
  private static final LookupTranslator DOUBLE_QUOTATION_ESCAPER =
      new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}});

  private final String index;
  private final String keyword;

  private int boost = -1;
  private boolean phrase = false;

  /**
   * 不指定索引的情况下，阿里云搜索会默认取default索引，若应用没有设置default索引则这种方式会报错
   * <p>
   * 参见：https://help.aliyun.com/document_detail/29191.html
   */
  public IndexQuery(String keyword) {
    this(null, keyword);
  }

  /**
   * @param index 为空，则openSearch默认取default索引
   */
  public IndexQuery(String index, String keyword) {
    checkArgument(StringUtils.isNotBlank(keyword), "keyword must not blank");
    this.index = index;
    this.keyword = keyword;
  }

  /**
   * 要设置的关键词权重，类型为int，范围为[0,99]，不设置默认为99。超出范围则取临界值
   */
  public IndexQuery setBoost(int boost) {
    if (boost < 0) {
      boost = 0;
    } else if (boost > 99) {
      boost = 99;
    }
    this.boost = boost;
    return this;
  }

  /**
   * 是否是短语查询，即要求查询词分词后需要各个term的位置相连、顺序一致
   */
  public IndexQuery setPhrase(boolean phrase) {
    this.phrase = phrase;
    return this;
  }

  @Override
  public StringBuilder appendQueryParams(StringBuilder sb) {
    if (StringUtils.isNotBlank(index)) {
      sb.append(index).append(":");
    }
    sb.append(getQuotedKeyword());
    if (boost != -1) {
      sb.append("^").append(boost);
    }
    return sb;
  }

  private String getQuotedKeyword() {
    if (phrase) {
      return String.format("\"%s\"", DOUBLE_QUOTATION_ESCAPER.translate(keyword));
    }
    return String.format("'%s'", SINGLE_QUOTATION_ESCAPER.translate(keyword));
  }

  @Override
  public boolean priorTo(LogicalOp op) {
    return true;
  }
}
