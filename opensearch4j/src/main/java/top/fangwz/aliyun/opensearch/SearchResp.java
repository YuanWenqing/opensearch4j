/**
 * @author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import top.fangwz.aliyun.opensearch.component.Facet;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yuanwq
 */
public class SearchResp<T> {
  private final List<T> values = Lists.newArrayList();
  private int total = -1;
  private String query;
  private Map<String, Facet> facetMap = Maps.newLinkedHashMap();

  private String rawResponse;
  private String status;
  private String requestId;
  private List<Error> errors = Lists.newArrayList();
  private List<ComputeCost> computeCosts = Lists.newArrayList();
  private String debugInfo;

  public void addValue(T value) {
    values.add(value);
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getTotal() {
    return total;
  }

  public List<T> getValues() {
    return values;
  }

  /**
   * SearchReq的文本
   */
  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void addFacet(Facet facet) {
    facetMap.put(facet.getGroupKey(), facet);
  }

  public Map<String, Facet> getFacetMap() {
    return Collections.unmodifiableMap(facetMap);
  }

  public Facet getFacet(String groupKey) {
    return facetMap.get(groupKey);
  }

  public void setRawResponse(String rawResponse) {
    this.rawResponse = rawResponse;
  }

  public String getRawResponse() {
    return rawResponse;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getRequestId() {
    return requestId;
  }

  public boolean isError() {
    return !status.equalsIgnoreCase("OK") || !errors.isEmpty();
  }

  public void addErrors(List<Error> errors) {
    this.errors.addAll(errors);
  }

  public List<Error> getErrors() {
    return errors;
  }

  public Error findError(int code) {
    for (Error error : errors) {
      if (error.getCode() == code) {
        return error;
      }
    }
    return null;
  }

  @Deprecated
  public String getErrMsg() {
    return String.valueOf(errors);
  }

  public void addComputeCosts(List<ComputeCost> computeCosts) {
    this.computeCosts.addAll(computeCosts);
  }

  public List<ComputeCost> getComputeCosts() {
    return computeCosts;
  }

  public ComputeCost findComputeCost(String indexName) {
    for (ComputeCost computeCost : computeCosts) {
      if (indexName.equals(computeCost.getIndexName())) {
        return computeCost;
      }
    }
    return null;
  }

  public String getDebugInfo() {
    return debugInfo;
  }

  public void setDebugInfo(String debugInfo) {
    this.debugInfo = debugInfo;
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean withData) {
    MoreObjects.ToStringHelper helper =
        MoreObjects.toStringHelper(getClass()).add("status", status).add("requestId", requestId)
            .add("query", "`" + query + "`").add("errors", errors).add("computeCost", computeCosts)
            .add("total", total);
    if (withData) {
      helper.add("values", values).add("facet", facetMap);
    }
    helper.add("debug", debugInfo);
    return helper.toString();
  }

  public static class Error {
    private final int code;
    private final String message;

    public Error(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      return "Errors{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
  }

  public static class ComputeCost {
    private final String indexName;
    private final double cost;

    public ComputeCost(String indexName, double cost) {
      this.indexName = indexName;
      this.cost = cost;
    }

    public double getCost() {
      return cost;
    }

    public String getIndexName() {
      return indexName;
    }

    @Override
    public String toString() {
      return "ComputeCost{" + "indexName='" + indexName + '\'' + ", cost=" + cost + '}';
    }
  }
}
