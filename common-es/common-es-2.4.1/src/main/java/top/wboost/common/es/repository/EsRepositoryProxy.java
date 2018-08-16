package top.wboost.common.es.repository;

import com.alibaba.fastjson.JSONObject;
import org.aopalliance.intercept.MethodInvocation;
import org.elasticsearch.index.query.QueryBuilders;
import top.wboost.common.base.page.BasePage;
import top.wboost.common.context.config.AutoProxy;
import top.wboost.common.es.search.EsAggregationSearch;
import top.wboost.common.es.search.EsQueryType;
import top.wboost.common.es.search.EsSearch;
import top.wboost.common.es.util.EsQueryUtil;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Map;

public class EsRepositoryProxy implements AutoProxy {

    private Logger log = LoggerUtil.getLogger(getClass());

    public static void main(String[] args) {
        EsAggregationSearch search = new EsAggregationSearch("trajx_znss_201807", "adm_gj");
        EsSearch searchChild = new EsSearch("trajx_znss_201807", "adm_cl");
        searchChild.special(EsQueryType.MUST).wildcard("cph", "02_浙A*");
        search.special(EsQueryType.MUST).child("adm_cl", searchChild);
        search.setType("adm_cl");
        search.setField("cph");
        System.out.println(JSONObject.toJSONString(EsQueryUtil.queryAggregationList(search, new BasePage(1, 2))));
    }

    public AutoProxy getObject(Class<?> clazz, Map<String, Object> config) throws Exception {
        EsRepositoryProxy proxy = new EsRepositoryProxy();
        QueryBuilders.termQuery("s", new ArrayList<String>());
        return proxy;
    }

    @Override public Object invoke(MethodInvocation invocation) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

}