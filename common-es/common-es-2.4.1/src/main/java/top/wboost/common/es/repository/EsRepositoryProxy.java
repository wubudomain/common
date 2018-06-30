package top.wboost.common.es.repository;

import java.util.ArrayList;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.elasticsearch.index.query.QueryBuilders;

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
        EsSearch search = new EsSearch("order", "order");
        search.special(EsQueryType.MUST).fuzzy("order_no", "1234");
        EsSearch searchChild = new EsSearch("order", "order_detail");
        search.child("order_detail", searchChild);
        searchChild.special(EsQueryType.MUST).fuzzy("commodity", "香");
        System.out.println(EsQueryUtil.querySimpleList(search, null));
    }

}