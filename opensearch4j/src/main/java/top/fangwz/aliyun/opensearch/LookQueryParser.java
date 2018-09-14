/**
 * @author yuanwq, date: 2017年9月13日
 */
package top.fangwz.aliyun.opensearch;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * look自定义的高级查询解释器，只支持一层，不支持嵌套。<br>
 * 同一个field前缀修饰的term会聚合成一个列表：比如{@code "f1:t1 f1:t2"} ==> {@code f1:[t1, t2,..]}
 * 
 * @author yuanwq
 */
public class LookQueryParser {
  private static final Logger logger = LoggerFactory.getLogger(LookQueryParser.class);

  public static final String DEFAULT_QUERY_FIELD = "query";
  // 不能使用WhiteSpace分隔的Analyzer，否则brand:"a b"中的“a b”会被分成两个term
  private static final Analyzer ANALYZER = new KeywordAnalyzer();

  private static class Holder {
    private static final LookQueryParser instance = new LookQueryParser(DEFAULT_QUERY_FIELD);
  }

  public static LookQueryParser getInstance() {
    return Holder.instance;
  }

  private final String queryField;

  /**
   * @param queryField 用于query的field名字
   */
  private LookQueryParser(String queryField) {
    this.queryField = queryField;
  }

  public String getQueryField() {
    return queryField;
  }

  private QueryParser getQueryParser() {
    // 非线程安全，故每次构造一个
    return new MultiFieldQueryParser(new String[] {queryField}, ANALYZER);
  }

  public ParsedQuery parse(String queryText) {
    ParsedQuery parsedQuery = new ParsedQuery(queryText);
    if (StringUtils.isBlank(queryText)) {
      return parsedQuery;
    }
    Query query;
    try {
      QueryParser parser = getQueryParser();
      query = parser.parse(queryText);
    } catch (ParseException e) {
      logger.warn("fail to parse: `" + queryText + "`", e);
      parsedQuery.setErrMsg(e.getMessage());
      parsedQuery.addTerm(queryField, queryText);
      return parsedQuery;
    }
    traverse(query, parsedQuery);
    return parsedQuery;
  }

  private void traverse(Query query, ParsedQuery parsedQuery) {
    if (query instanceof TermQuery) {
      TermQuery termQuery = (TermQuery) query;
      parsedQuery.addTerm(termQuery.getTerm().field(), termQuery.getTerm().text());
      return;
    } else if (query instanceof BooleanQuery) {
      BooleanQuery booleanQuery = (BooleanQuery) query;
      for (BooleanClause clause : booleanQuery.clauses()) {
        traverse(clause.getQuery(), parsedQuery);
      }
      return;
    } else {
      logger.warn("unknown query type: {}, query=`{}`, rawQuery: `{}`", query.getClass(), query,
          parsedQuery.getRawQuery());
    }
  }

}
