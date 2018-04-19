package top.wboost.common.kylin.api.search;

import org.springframework.http.HttpMethod;

import top.wboost.common.kylin.api.KylinApi;
import top.wboost.common.kylin.search.KylinSearch;

public abstract class KylinApiSearch implements KylinSearch {

    protected KylinApi kylinApi;

    public KylinApiSearch(KylinApi kylinApi) {
        super();
        this.kylinApi = kylinApi;
    }

    @Override
    public HttpMethod getMethod() {
        return kylinApi.getHttpMethod();
    }

    @Override
    public String getUrl() {
        return kylinApi.getUrl();
    }
}
