package top.wboost.common.utils.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.StringUtil;

/**
 * 扫描配置文件
 * @author jwSun
 * @date 2017年3月4日 上午10:02:27
 */
@Deprecated
public class PropertiesUtil3 extends PropertyPlaceholderConfigurer {

    private static Logger log = LoggerUtil.getLogger(PropertiesUtil.class);

    private static final String DEFAULT_PROPERTIES = "properties/common-default.properties";
    private static final String DEFAULT_PROPERTIES_SCAN = "properties/*.properties";
    private static final String SYS_PROPERTIES_SCAN = "sys/properties/*.properties";
    private static final String DEFAULT_CONFIG_FILE = "properties/config.properties";
    private static final String DEFAULT_CONFIG_NAME = "sys.properties";
    // 匹配${}
    private static final String PATTERN_COMPILE = "\\$\\{([^\\$]+?)\\}";
    private static final String SPLIT = ":";
    private static String patternComolie = PATTERN_COMPILE;
    private static String propertiesFile = DEFAULT_PROPERTIES;
    public static Map<String, String> propertyMap;
    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    static {
        propertyMap = new ConcurrentHashMap<String, String>();
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
    }

    protected Properties mergeProperties() throws IOException {
        Properties result = super.mergeProperties();
        mergeProperties(result, addProperties(DEFAULT_PROPERTIES_SCAN));
        mergeProperties(result, addProperties(SYS_PROPERTIES_SCAN));
        String properties = getProperty(DEFAULT_CONFIG_NAME, DEFAULT_CONFIG_FILE);
        if (StringUtil.notEmpty(properties)) {
            String[] locations = StringUtils.commaDelimitedListToStringArray(properties);
            for (String location : locations) {
                mergeProperties(result, addProperties(location));
            }
        }
        replaceProperties();
        if (log.isInfoEnabled()) {
            log.info(JSONObject.toJSONString(propertyMap));
        }
        Properties retresult = new Properties();
        propertyMap.forEach((key, val) -> {
            retresult.put(key, val);
        });
        return retresult;
    }

    public static Properties mergeProperties(Properties p1, Properties p2) throws IOException {
        p2.forEach((obj1, obj2) -> {
            p1.put(obj1, obj2);
        });
        return p1;
    }

    // 递归替换参数带有${}的值(spring 不会转换)
    private void replaceProperties() {
        int okVal = 0;
        // 遍历每个key查询是否有未转化的${}
        for (String key : propertyMap.keySet()) {
            String value = propertyMap.get(key);
            if (value == null || "".equals(value)) {
                okVal++;
                continue;
            }
            // 获得未转化的${} 内容集合
            List<String> list = StringUtil.getPatternMattcherList(value, patternComolie, 0);
            if (list.size() > 0) {
                value = replace(value);
            } else {
                okVal++;
            }
            propertyMap.put(key, value);
        }
        if (okVal == propertyMap.size()) {
            return;
        } else {
            // 递归替换
            replaceProperties();
        }
    }

    private static String replace(String value) {
        List<String> list = StringUtil.getPatternMattcherList(value, patternComolie, 0);
        if (list.size() > 0) {
            log.debug("获得未转化的${} 内容集合 : " + list.toString());
            for (String str : list) {
                String configName = str.substring(2);
                configName = configName.substring(0, configName.length() - 1);
                String pattern = "\\$\\{" + configName + "\\}";
                String realVal = getPropertiesByConfig(configName);
                if (StringUtil.notEmpty(realVal)
                        && StringUtil.getPatternMattcherList(realVal, patternComolie, 1).size() > 0) {
                    continue;
                }
                try {
                    value = value.replaceAll(pattern, realVal);
                } catch (Exception o_o) {
                    log.error("replace value: {} , pattern: {}, realVal: {}", value, pattern, realVal);
                    throw o_o;
                }
            }
        }
        return value;
    }

    // 获得配置文件值
    public static String getProperty(String name) {
        name = replace(name);
        if (propertyMap != null && propertyMap.containsKey(name)) {
            return propertyMap.get(name);
        }
        return getProperty(name, propertiesFile);
    }

    public static String getProperty(String name, String path) {
        name = replace(name);
        Properties properties = loadProperties(path);
        if (properties != null) {
            return (String) properties.get(name);
        }
        log.warn("name {},path: {} properties is undefined", name, path);
        return null;
    }

    public static String getPropertyOrDefault(String name, String defaultVal) {
        String value = getProperty(name);
        return value != null ? value : defaultVal;
    }

    public static String getPropertyOrDefault(String name, String path, String defaultVal) {
        String value = getProperty(name, path);
        return value != null ? value : defaultVal;
    }

    public static Properties loadProperties(String path) {
        InputStream in = null;
        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
            Properties prop = new Properties();
            prop.load(new InputStreamReader(in, "UTF-8"));
            return prop;
        } catch (Exception e) {
            log.warn("loadProperties path:{} , {}", path, e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    /**
     * 获得指定前缀的集合
     * @param prefix 前缀
     * @return
     */
    public static Map<String, String> getPropertiesByPrefix(String prefix) {
        Map<String, String> retMap = new HashMap<>();
        for (Entry<String, String> entry : propertyMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                retMap.put(entry.getKey(), entry.getValue());
            }
        }
        return retMap;
    }

    /**
     * 增加配置
     * @author jwSun
     * @date 2017年6月22日 下午5:58:02
     * @param resources
     */
    public Properties addProperties(Resource... resources) {
        Properties properties = new Properties();
        try {
            for (Resource resource : resources) {
                InputStream stream = resource.getInputStream();
                properties.load(stream);
                for (Entry<Object, Object> entry : properties.entrySet()) {
                    propertyMap.put(entry.getKey().toString(), entry.getValue().toString());
                }
                IOUtils.closeQuietly(stream);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        replaceProperties();
        return properties;
    }

    /*@SuppressWarnings("unchecked")
    @Override
    public void apply(Object location) {
        try {
            log.info("load properties {}", location);
            if (location instanceof List) {
                List<String> locationList = (List<String>) location;
                for (String oneLocation : locationList) {
                    addProperties(oneLocation);
                }
            } else if (location instanceof String) {
                addProperties(location.toString());
            }
        } catch (Exception e) {
            log.error("apply error,location is {} ,error is {}", location, e.getMessage());
        }
        replaceProperties();
    }*/

    /**
     * 增加配置至map
     * @param location
     */
    private Properties addProperties(String location) {
        try {
            Resource[] resources = resolver.getResources(location);
            return addProperties(resources);
        } catch (Exception e) {
            log.warn("addProperties:{} error,exteption is {}", location, e.getLocalizedMessage());
        }
        return new Properties();
    }

    private static String getPropertiesByConfig(String propertiesKey) {
        String settingKey = propertiesKey;
        String defaultKey = null;
        if (settingKey.contains(":")) {
            defaultKey = settingKey.substring(settingKey.indexOf(SPLIT) + 1);
            settingKey = settingKey.substring(0, settingKey.indexOf(SPLIT));
        }
        return getPropertyOrDefault(settingKey, defaultKey);
    }

}
