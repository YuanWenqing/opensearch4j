package top.fangwz.aliyun.opensearch;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author: yuanwq
 * @date: 2018/9/22
 */
public interface ItemParser<T> {
  T parse(JsonNode node);
}
