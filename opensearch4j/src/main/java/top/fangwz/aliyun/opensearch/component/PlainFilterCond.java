package top.fangwz.aliyun.opensearch.component;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.fangwz.aliyun.opensearch.IFilterCond;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/9/21
 */
@Data
public class PlainFilterCond implements IFilterCond {
  private final String plainCond;

  private PlainFilterCond(String plainCond) {
    checkArgument(StringUtils.isNotBlank(plainCond), "plain cond must not blank");
    this.plainCond = plainCond;
  }

  @Override
  public boolean priorTo(LogicalOp op) {
    return false;
  }

  @Override
  public StringBuilder appendSearchParams(StringBuilder sb) {
    sb.append(plainCond);
    return sb;
  }

  public static PlainFilterCond of(String plainCond) {
    return new PlainFilterCond(plainCond);
  }
}
