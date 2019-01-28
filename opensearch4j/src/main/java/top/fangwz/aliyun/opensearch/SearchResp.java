/**
 * author yuanwq, date: 2017年9月12日
 */
package top.fangwz.aliyun.opensearch;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * author yuanwq
 */
@Data
public class SearchResp<T> {

  private Status status;
  private String requestId;
  private Result<T> result;
  private final List<Error> errors = Lists.newArrayList();
  private String debugInfo;

  /**
   * SearchReq的文本
   */
  private String query;
  private String rawResponse;

  public void addErrors(Collection<Error> errors) {
    this.errors.addAll(errors);
  }

  public Error findError(int code) {
    for (Error error : errors) {
      if (error.getCode() == code) {
        return error;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean withDetails) {
    MoreObjects.ToStringHelper helper =
        MoreObjects.toStringHelper(getClass()).add("status", status).add("requestId", requestId)
            .add("query", "`" + query + "`").add("errors", errors);
    if (result != null) {
      helper.add("result", result.toString(withDetails));
    }
    helper.add("debug", debugInfo);
    return helper.toString();
  }

  public enum Status {
    OK,
    FAIL
  }

  @Data
  public static class Result<T> {
    /**
     * 引擎耗时，单位秒
     */
    private double searchTime;
    private int total;
    private int num;
    private int viewTotal;
    private final List<ComputeCost> computeCosts = Lists.newArrayList();
    private final List<T> items = Lists.newArrayList();
    private final Map<String, Facet> facetMap = Maps.newLinkedHashMap();

    public void addComputeCosts(Collection<ComputeCost> computeCosts) {
      this.computeCosts.addAll(computeCosts);
    }

    public ComputeCost findComputeCost(String indexName) {
      for (ComputeCost computeCost : computeCosts) {
        if (indexName.equals(computeCost.getIndexName())) {
          return computeCost;
        }
      }
      return null;
    }

    public void addItem(T item) {
      items.add(item);
    }

    public void addFacet(Facet facet) {
      facetMap.put(facet.getGroupKey(), facet);
    }

    public Facet getFacet(String groupKey) {
      return facetMap.get(groupKey);
    }

    @Override
    public String toString() {
      return toString(false);
    }

    public String toString(boolean withDetails) {
      return "Result{" + "searchTime=" + searchTime + ", total=" + total + ", num=" + num +
          ", viewTotal=" + viewTotal + ", computeCosts=" + computeCosts +
          (withDetails ? ", items=" + items + ", facetMap=" + facetMap : "") + '}';
    }
  }

  @Data
  public static class ComputeCost {
    private final String indexName;
    private final double cost;

  }
}
