package top.wboost.common.context.classLoader;

import com.alibaba.fastjson.JSONArray;

import top.wboost.common.utils.web.utils.PropertiesUtil;

public class NetContextLoader {

    static {
        String urls = PropertiesUtil.getProperty("system.class.loader.urls");
        JSONArray downFile = JSONArray.parseArray(urls);

    }

}
