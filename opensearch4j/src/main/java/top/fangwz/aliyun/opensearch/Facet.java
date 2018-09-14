/**
 * @author yuanwq, date: 2017年9月15日
 */
package top.fangwz.aliyun.opensearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yuanwq
 */
public class Facet {
  private final String groupKey;
  private final Map<String, FacetItem> itemMap = Maps.newLinkedHashMap();

  public Facet(String groupKey) {
    this.groupKey = groupKey;
  }

  public Facet(JsonNode facetNode) {
    this(facetNode.get("key").asText());
    for (JsonNode node : (ArrayNode) facetNode.get("items")) {
      String itemValue = node.get("value").asText();
      FacetItem item = new FacetItem(itemValue);
      itemMap.put(itemValue, item);
      Iterator<String> iter = node.fieldNames();
      while (iter.hasNext()) {
        String statName = iter.next();
        if ("value".equals(statName)) {
          continue;
        }
        item.statMap.put(statName, node.get(statName).asLong());
      }
    }
  }

  public String getGroupKey() {
    return groupKey;
  }

  public Map<String, FacetItem> getItemMap() {
    return Collections.unmodifiableMap(itemMap);
  }

  public FacetItem getItem(String itemValue) {
    return itemMap.get(itemValue);
  }

  @Override
  public String toString() {
    return String.format("{groupKey=%s, items=%s}", groupKey, itemMap.values());
  }

  public static class FacetItem {
    private final String value;
    private final Map<String, Number> statMap = Maps.newLinkedHashMap();

    public FacetItem(String itemValue) {
      this.value = itemValue;
    }

    public String getValue() {
      return value;
    }

    public Map<String, Number> getStatMap() {
      return Collections.unmodifiableMap(statMap);
    }

    public Number getStat(String statName) {
      return statMap.get(statName);
    }

    @Override
    public String toString() {
      return String.format("%s=%s", value, statMap);
    }
  }
}
