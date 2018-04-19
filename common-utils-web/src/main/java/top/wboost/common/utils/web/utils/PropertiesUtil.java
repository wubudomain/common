package top.wboost.common.utils.web.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import top.wboost.common.base.enums.CharsetEnum;
import top.wboost.common.exception.BusinessException;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.core.ConfigProperties;

public class PropertiesUtil {

    private static Logger log = LoggerUtil.getLogger(PropertiesUtil.class);

    private static PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * 获得配置文件参数
     * @param name 配置名
     * @return 配置值
     */
    public static String getProperty(String name) {
        return getPropertyOrDefault(name, null, null);
    }

    public static String getProperty(String name, String path) {
        return getPropertyOrDefault(name, path, null);
    }

    public static String getPropertyOrDefault(String name, String defaultVal) {
        return getPropertyOrDefault(name, null, defaultVal);
    }

    public static String getPropertyOrDefault(String name, String path, String defaultVal) {
        String val = null;
        try {
            if (!StringUtil.notEmpty(path)) {
                if (ConfigProperties.resolver != null) {
                    val = ConfigProperties.resolver.resolveStringValue("${" + name + "}");
                    if (val != null)
                        val = new String(val.getBytes(CharsetEnum.ISO_8859_1.getName()),
                                CharsetEnum.UTF_8.getCharset());
                } else {
                    val = ConfigProperties.localenv.getProperty(name, defaultVal);
                }
            } else {
                try {
                    EncodedResource[] resources = loadResources(path);
                    for (EncodedResource resource : resources) {
                        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                        Object obj = properties.get(name);
                        if (obj != null) {
                            val = obj.toString();
                            break;
                        }
                    }
                } catch (IOException e) {
                    if (log.isWarnEnabled()) {
                        log.warn(e.getLocalizedMessage());
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return val == null ? defaultVal : val;
    }

    public static Properties loadProperties(String location) {
        EncodedResource[] resources = loadResources(location);
        Properties prop = new Properties();
        try {
            for (EncodedResource resource : resources) {
                prop.putAll(PropertiesLoaderUtils.loadProperties(resource));
            }
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Properties();
    }

    /**
     * 获得指定前缀的集合
     * @param prefix 前缀
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getPropertiesByPrefix(String prefix) {
        Map<String, Object> retMap = new HashMap<>();
        for (Entry<String, Object> entry : getAllProperties().entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                retMap.put(entry.getKey(), entry.getValue());
            }
        }
        return retMap;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getAllProperties() {
        Map<String, Object> retMap = new HashMap<>();
        Iterator<org.springframework.core.env.PropertySource<?>> ite = ConfigProperties.localenv.getPropertySources()
                .iterator();
        while (ite.hasNext()) {
            org.springframework.core.env.PropertySource<?> s = ite.next();
            if (s.getSource() instanceof Map) {
                retMap.putAll((Map<? extends String, ? extends String>) s.getSource());
            } else if (s instanceof EnumerablePropertySource) {
                EnumerablePropertySource<Collection<PropertySource<?>>> source = (EnumerablePropertySource<Collection<PropertySource<?>>>) s;
                String[] nameArray = source.getPropertyNames();
                Arrays.asList(nameArray).forEach(name -> {
                    retMap.put(name, s.getProperty(name));
                });
            }
        }
        return retMap;
    }

    private static EncodedResource[] loadResources(String location) {
        try {
            Resource[] resources = resourceResolver.getResources(location);
            EncodedResource[] encodeResources = new EncodedResource[resources.length];
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                encodeResources[i] = new EncodedResource(resource, CharsetEnum.UTF_8.getCharset());
            }
            return encodeResources;
        } catch (Exception e) {
            log.error("loadResource error", e);
            throw new BusinessException("loadResource error");
        }
    }

    public static Object getPropertiesObject(String name) {
        return getAllProperties().get(name);
    }

}
