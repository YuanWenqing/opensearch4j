package top.fangwz.aliyun.opensearch.expr;

/**
 * @author: yuanwq
 * @date: 2018/9/21
 */
public interface RelationalFilters {
  interface Values {
    static RelationalCond eq(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.EQ, Value.of(value));
    }

    static RelationalCond ne(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.NE, Value.of(value));
    }

    static RelationalCond lt(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.LT, Value.of(value));
    }

    static RelationalCond lte(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.LTE, Value.of(value));
    }

    static RelationalCond gt(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.GT, Value.of(value));
    }

    static RelationalCond gte(String field, Object value) {
      return new RelationalCond(Field.of(field), RelationalCond.RelationalOp.GTE, Value.of(value));
    }
  }

  interface Fields {
    static RelationalCond eq(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.EQ, Field.of(field2));
    }

    static RelationalCond ne(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.NE, Field.of(field2));
    }

    static RelationalCond lt(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.LT, Field.of(field2));
    }

    static RelationalCond lte(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.LTE,
          Field.of(field2));
    }

    static RelationalCond gt(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.GT, Field.of(field2));
    }

    static RelationalCond gte(String field1, String field2) {
      return new RelationalCond(Field.of(field1), RelationalCond.RelationalOp.GTE,
          Field.of(field2));
    }
  }

  interface Expressions {
    static RelationalCond eq(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.EQ, right);
    }

    static RelationalCond ne(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.NE, right);
    }

    static RelationalCond lt(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.LT, right);
    }

    static RelationalCond lte(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.LTE, right);
    }

    static RelationalCond gt(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.GT, right);
    }

    static RelationalCond gte(IExpression left, IExpression right) {
      return new RelationalCond(left, RelationalCond.RelationalOp.GTE, right);
    }
  }
}
