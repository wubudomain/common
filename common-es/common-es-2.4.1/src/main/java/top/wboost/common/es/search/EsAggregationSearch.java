package top.wboost.common.es.search;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;

import lombok.Data;
import top.wboost.common.es.entity.EsCountFilter;

/**
 * Es聚合查询类
 * @author jwSun
 * @date 2017年6月21日 上午11:33:58
 */
public class EsAggregationSearch extends EsSearch {

    /**
     * 查询结果条数
     */
    private int size = 10;
    /**
     * 如count>0 && count <10
     */
    private String inline;
    /**
     * 暂未实现,默认大->小
     */
    private Map<String, SortOrder> orderMap = new HashMap<String, SortOrder>();
    /**
     * 聚合条数过滤
     */
    private EsCountFilter esCountFilter;

    private AggsEntity aggs = new AggsEntity();

    @Data
    public static class AggsEntity {
        // 聚合属性名
        String field;
        //若无type则为父查询
        String type;
    }

    public EsAggregationSearch(String index, String type) {
        super(index, type);
    }

    public EsAggregationSearch(String index, String type, String field) {
        super(index, type);
        this.aggs.setField(field);
    }

    public EsAggregationSearch setField(String field) {
        this.aggs.setField(field);
        return this;
    }

    public EsAggregationSearch setType(String type) {
        this.aggs.setType(type);
        return this;
    }

    public EsAggregationSearch setSize(int size) {
        this.size = size;
        return this;
    }

    public EsAggregationSearch setInline(String inline) {
        this.inline = inline;
        return this;
    }

    public EsAggregationSearch setOrderMap(Map<String, SortOrder> orderMap) {
        this.orderMap = orderMap;
        return this;
    }

    public EsAggregationSearch setEsCountFilter(EsCountFilter esCountFilter) {
        this.esCountFilter = esCountFilter;
        return this;
    }

    public AggsEntity getAggs() {
        return aggs;
    }

    public int getSize() {
        return size;
    }

    public String getInline() {
        return inline;
    }

    public Map<String, SortOrder> getOrderMap() {
        return orderMap;
    }

    public EsCountFilter getEsCountFilter() {
        return esCountFilter;
    }

}
