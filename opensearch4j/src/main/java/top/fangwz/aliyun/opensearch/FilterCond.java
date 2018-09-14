/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.Function;

import java.util.Collection;

/**
 * @author yuanwq
 */
public class FilterCond extends LogicCond<IFilterCond, FilterCond> implements IClause, IFilterCond {

  public static FilterCond and() {
    return new FilterCond(LogicOp.AND);
  }

  public static <T> FilterCond and(Collection<T> values, Function<T, IFilterCond> unitMaker) {
    FilterCond cond = and();
    for (T v : values) {
      cond.add(unitMaker.apply(v));
    }
    return cond;
  }

  public static FilterCond or() {
    return new FilterCond(LogicOp.OR);
  }

  public static <T> FilterCond or(Collection<T> values, Function<T, IFilterCond> unitMaker) {
    FilterCond cond = or();
    for (T v : values) {
      cond.add(unitMaker.apply(v));
    }
    return cond;
  }

  public static FilterCond andnot() {
    return new FilterCond(LogicOp.ANDNOT);
  }

  public static FilterCond rank() {
    return new FilterCond(LogicOp.RANK);
  }

  FilterCond(LogicOp op) {
    super(op);
  }

}
