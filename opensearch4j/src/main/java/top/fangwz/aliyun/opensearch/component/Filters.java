package top.fangwz.aliyun.opensearch.component;

import top.fangwz.aliyun.opensearch.IFilterCond;

import java.util.Collection;
import java.util.Iterator;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/9/21
 */
public interface Filters {
  interface Values {
    static RelationalFilterCond eq(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.EQ,
          Value.of(value));
    }

    static RelationalFilterCond ne(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.NE,
          Value.of(value));
    }

    static RelationalFilterCond lt(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.LT,
          Value.of(value));
    }

    static RelationalFilterCond lte(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.LTE,
          Value.of(value));
    }

    static RelationalFilterCond gt(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.GT,
          Value.of(value));
    }

    static RelationalFilterCond gte(String field, Object value) {
      return new RelationalFilterCond(Field.of(field), RelationalFilterCond.RelationalOp.GTE,
          Value.of(value));
    }
  }

  interface Fields {
    static RelationalFilterCond eq(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.EQ,
          Field.of(field2));
    }

    static RelationalFilterCond ne(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.NE,
          Field.of(field2));
    }

    static RelationalFilterCond lt(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.LT,
          Field.of(field2));
    }

    static RelationalFilterCond lte(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.LTE,
          Field.of(field2));
    }

    static RelationalFilterCond gt(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.GT,
          Field.of(field2));
    }

    static RelationalFilterCond gte(String field1, String field2) {
      return new RelationalFilterCond(Field.of(field1), RelationalFilterCond.RelationalOp.GTE,
          Field.of(field2));
    }
  }

  interface Expressions {
    static RelationalFilterCond eq(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.EQ, right);
    }

    static RelationalFilterCond ne(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.NE, right);
    }

    static RelationalFilterCond lt(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.LT, right);
    }

    static RelationalFilterCond lte(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.LTE, right);
    }

    static RelationalFilterCond gt(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.GT, right);
    }

    static RelationalFilterCond gte(IExpression left, IExpression right) {
      return new RelationalFilterCond(left, RelationalFilterCond.RelationalOp.GTE, right);
    }
  }

  interface Conds {
    static IFilterCond and(Collection<? extends IFilterCond> subConds) {
      checkArgument(!subConds.isEmpty(), "no sub cond");
      Iterator<? extends IFilterCond> iterator = subConds.iterator();
      IFilterCond cond = iterator.next();
      while (iterator.hasNext()) {
        cond = new LogicalFilterCond(cond, IFilterCond.LogicalOp.AND, iterator.next());
      }
      return cond;
    }

    static IFilterCond or(Collection<? extends IFilterCond> subConds) {
      checkArgument(!subConds.isEmpty(), "no sub cond");
      Iterator<? extends IFilterCond> iterator = subConds.iterator();
      IFilterCond cond = iterator.next();
      while (iterator.hasNext()) {
        cond = new LogicalFilterCond(cond, IFilterCond.LogicalOp.OR, iterator.next());
      }
      return cond;
    }
  }
}
