/**
 * author yuanwq, date: 2017年9月15日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * author yuanwq
 */
@Data
public class Facet {
  private final String groupKey;
  private final Map<String, FacetItem> itemMap = Maps.newLinkedHashMap();

  public Facet(String groupKey) {
    this.groupKey = groupKey;
  }

  public FacetItem getItem(String itemValue) {
    return itemMap.get(itemValue);
  }

  public void putItem(String itemValue, FacetItem item) {
    this.itemMap.put(itemValue, item);
  }

  @Data
  public static class FacetItem {
    private final String value;
    private final Map<String, Number> aggregateValueMap = Maps.newLinkedHashMap();

    public FacetItem(String itemValue) {
      this.value = itemValue;
    }

    public Number getAggregateValue(String aggFuncName) {
      return aggregateValueMap.get(aggFuncName);
    }

    public void putAggregateValue(String aggFuncName, Number value) {
      this.aggregateValueMap.put(aggFuncName, value);
    }
  }
}
