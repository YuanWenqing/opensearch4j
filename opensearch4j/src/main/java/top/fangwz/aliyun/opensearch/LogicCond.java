/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @param <U> 逻辑单元的类型
 * @param <T> 子类的类型
 * @author yuanwq
 */
public abstract class LogicCond<U extends ICond<U>, T extends LogicCond>
    implements ISearchClause {
  protected static enum LogicOp {
    AND,
    OR,
    ANDNOT,
    RANK
  }

  protected final List<U> conds = Lists.newArrayList();
  protected final LogicOp op;
  protected final Set<String> fields = Sets.newLinkedHashSet();
  protected boolean empty = true;

  protected LogicCond(LogicOp op) {
    this.op = op;
  }

  public T add(U cond) {
    conds.add(cond);
    fields.addAll(cond.getAllFields());
    this.empty = this.empty && cond.isEmpty();
    return (T) this;
  }

  public T addAll(Collection<U> conds) {
    conds.forEach(this::add);
    return (T) this;
  }

  public boolean isEmpty() {
    return empty;
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    if (conds.size() == 0) {
      return sb;
    }
    boolean first = true;
    for (U cond : conds) {
      if (cond.isEmpty()) {
        continue;
      }
      if (first) {
        first = false;
      } else {
        sb.append(" ").append(op.name()).append(" ");
      }
      boolean ifWrap = needWrap(cond);
      if (ifWrap) {
        sb.append("(");
      }
      cond.appendSearchParams(sb);
      if (ifWrap) {
        sb.append(")");
      }
    }
    return sb;
  }

  protected boolean needWrap(U cond) {
    if (cond instanceof LogicCond) {
      LogicCond that = (LogicCond) cond;
      return this.op.ordinal() < that.op.ordinal();
    }
    return false;
  }

  @Override
  public String toString() {
    return appendSearchParams(new StringBuilder()).toString();
  }

  public Set<String> getAllFields() {
    return Collections.unmodifiableSet(fields);
  }
}
