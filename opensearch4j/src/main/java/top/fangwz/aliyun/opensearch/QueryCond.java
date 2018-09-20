/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.Function;

import java.util.Collection;

/**
 * @author yuanwq
 */
public class QueryCond extends LogicCond<IQueryCond, QueryCond>
    implements ISearchClause, IQueryCond {

  public static QueryCond and() {
    return new QueryCond(LogicOp.AND);
  }

  public static <T> QueryCond and(Collection<T> values, Function<T, IQueryCond> unitMaker) {
    QueryCond cond = and();
    for (T v : values) {
      cond.add(unitMaker.apply(v));
    }
    return cond;
  }

  public static QueryCond or() {
    return new QueryCond(LogicOp.OR);
  }

  public static <T> QueryCond or(Collection<T> values, Function<T, IQueryCond> unitMaker) {
    QueryCond cond = or();
    for (T v : values) {
      cond.add(unitMaker.apply(v));
    }
    return cond;
  }

  public static QueryCond andnot() {
    return new QueryCond(LogicOp.ANDNOT);
  }

  public static QueryCond rank() {
    return new QueryCond(LogicOp.RANK);
  }

  QueryCond(LogicOp op) {
    super(op);
  }

}
