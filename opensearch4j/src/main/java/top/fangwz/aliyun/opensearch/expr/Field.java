package top.fangwz.aliyun.opensearch.expr;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/9/20
 */
@Data
public class Field implements IExpression {
  private final String field;

  private Field(String field) {
    checkArgument(StringUtils.isNotBlank(field), "field must not blank");
    this.field = field;
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append(field);
    return sb;
  }

  public static Field of(String field) {
    return new Field(field);
  }
}
