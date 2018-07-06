package top.wboost.common.es.repository;

import java.util.ArrayList;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.elasticsearch.index.query.QueryBuilders;

import com.alibaba.fastjson.JSONObject;

import top.wboost.common.base.page.BasePage;
import top.wboost.common.context.config.AutoProxy;
import top.wboost.common.es.search.EsQueryType;
import top.wboost.common.es.search.EsSearch;
import top.wboost.common.es.util.EsQueryUtil;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;

public class EsRepositoryProxy implements AutoProxy {

    private Logger log = LoggerUtil.getLogger(getClass());

    public AutoProxy getObject(Class<?> clazz, Map<String, Object> config) throws Exception {
        EsRepositoryProxy proxy = new EsRepositoryProxy();
        QueryBuilders.termQuery("s", new ArrayList<String>());
        return proxy;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
        EsSearch search = new EsSearch("trajx_znss_201806", "adm_gj");
        EsSearch searchChild = new EsSearch("trajx_znss_201806", "adm_mac");
        searchChild.must("mac", "C1-C4-E7-02-13-95");
        EsSearch b = new EsSearch("t1", "t2").must("xxdz", "丰潭路萍水路南口");
        EsSearch a = new EsSearch("t1", "t2").must("xxdz", "丰潭路萍水路北口");
        search.special(EsQueryType.MUST).child("adm_mac", searchChild).or(a, b);
        System.out.println(JSONObject.toJSONString(EsQueryUtil.querySimpleList(search, new BasePage(1, 2))));
    }

}