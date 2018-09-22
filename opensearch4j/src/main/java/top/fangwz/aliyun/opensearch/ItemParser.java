package top.fangwz.aliyun.opensearch;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author: yuanwq
 * @date: 2018/9/22
 */
public interface ItemParser<T> {
  T parse(JsonNode node);

  static ItemParser<String> single(String field) {
    return node -> {
      JsonNode valueNode = node.get(field);
      return valueNode == null ? null : valueNode.asText();
    };
  }
}
