/**
 * @author yuanwq, date: 2017年9月15日
 */
package top.fangwz.aliyun.opensearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;

/**
 * @author yuanwq
 */
@Data
public class Facet {
  private final String groupKey;
  private final Map<String, FacetItem> itemMap = Maps.newLinkedHashMap();

  public Facet(String groupKey) {
    this.groupKey = groupKey;
  }

  public static Facet of(JsonNode facetNode) {
    // TODO: SearchRespParser
    Facet facet = new Facet(facetNode.get("key").asText());
    for (JsonNode node : facetNode.get("items")) {
      String itemValue = node.get("value").asText();
      FacetItem item = new FacetItem(itemValue);
      facet.itemMap.put(itemValue, item);
      Iterator<String> iter = node.fieldNames();
      while (iter.hasNext()) {
        String aggFuncName = iter.next();
        if ("value".equals(aggFuncName)) {
          continue;
        }
        item.aggregateValueMap.put(aggFuncName, node.get(aggFuncName).asLong());
      }
    }
    return facet;
  }

  public FacetItem getItem(String itemValue) {
    return itemMap.get(itemValue);
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

  }
}
