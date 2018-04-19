package top.wboost.base.spring.boot.starter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import top.wboost.common.util.CopyUtil;
import top.wboost.common.util.ReflectUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.utils.ConvertUtil;
import top.wboost.common.utils.web.utils.PropertiesUtil;

/**
 * 自定义配置文件树解析
 * @className CustomerPropertiesTreeUtil
 * @author jwSun
 * @date 2018年4月15日 上午11:03:24
 * @version 1.0.0
 */
public class CustomerPropertiesTreeUtil {

    /**
     * <pre>
     * common: 
          datasource: 
            driver-class-name: oracle.jdbc.driver.OracleDriver
            primary: 
              url: jdbc:oracle:thin:@192.168.1.225:1521:orcl
              username: trajx_dev
              password: trajx334
            d1: 
              url: http
              username: name1
              password: password1
            d2: 
              url: https
              username: name2
              password: password2
        
        get Map <{primary,clazz}{d1,clazz},{d2,clazz}>
    </pre>
     * @param clazz
     * @param prop
     * @param prefix
     * @param defaultName 若没有对应的配置树，则将传入的对象存入一个此名字为key的对象
     * @param filterName
     * @return
     */
    @SuppressWarnings("deprecation")
    public static <T> Map<String, T> resolvePropertiesTree(Class<T> clazz, T prop, String prefix, String defaultName,
            String... filterName) {
        Map<String, Object> resolveConfigs = PropertiesUtil.getPropertiesByPrefix(prefix);
        converterArray(resolveConfigs);
        String pattern = prefix.replaceAll("\\.", "\\\\.") + "\\.(.+?)\\.(.+)";
        Map<String, T> resolvers = new HashMap<>();
        resolveConfigs.forEach((String name, Object value) -> {
            if (StringUtil.getPatternMattcherList(name, pattern, 0).size() == 0) {
                return;
            }
            String dsName = StringUtil.getPatternMattcherList(name, pattern, 1).get(0);
            String dsValue = column2field(StringUtil.getPatternMattcherList(name, pattern, 2).get(0));
            if (resolvers.get(dsName) == null) {
                resolvers.put(dsName, CopyUtil.copyBean(clazz, prop, filterName));
            }
            Method dsPropMethod = ReflectUtil.getWriteMethod(clazz, dsValue);
            if (dsPropMethod != null) {
                Class<?> parameterType = dsPropMethod.getParameterTypes()[0];
                if (parameterType.isArray()) {
                    if (!value.getClass().isArray()) {
                        value = new Object[] { value };
                    }
                    value = ConvertUtil.parseArraytoClassArray(value, parameterType.getComponentType());
                }
                Object param = value;
                try {
                    dsPropMethod.invoke(resolvers.get(dsName), param);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (resolvers.size() == 0) {
            resolvers.put(defaultName, prop);
        }
        return resolvers;
    }

    private static void converterArray(Map<String, Object> configs) {
        Map<String, List<Integer>> map = new HashMap<>();
        for (Entry<String, Object> entry : configs.entrySet()) {
            int right = entry.getKey().indexOf("]");
            int left = entry.getKey().indexOf("[");
            if (right != -1 && left != -1) {
                String propName = entry.getKey().substring(0, left);
                if (!map.containsKey(propName)) {
                    map.put(propName, new ArrayList<>());
                }
                map.get(propName).add(Integer.parseInt(entry.getKey().substring(left + 1, right)));
            }
        }
        map.forEach((propName, indexList) -> {
            indexList.sort(((l, r) -> {
                return l - r;
            }));
            List<Object> list = new ArrayList<>();
            indexList.forEach(index -> {
                list.add(configs.get(propName + "[" + index + "]"));
                configs.remove(propName + "[" + index + "]");
            });
            configs.put(propName, list.toArray());
        });

    }

    /**
     * xx-xx-xx -> xxXxXx
     * @param columnName
     * @return
     */
    public static String column2field(String columnName) {
        if (columnName == null || "".equals(columnName)) {
            return null;
        }
        String[] arr = columnName.split("-");
        if (arr == null || arr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)));
            sb.append(arr[i].substring(1));

        }
        return sb.toString();
    }

    private String[] s;

    public String[] getS() {
        return s;
    }

    public void setS(String[] s) {
        this.s = s;
    }

    public static void main(String[] args) throws NoSuchFieldException, SecurityException {
        System.out.println(ReflectUtil.getWriteMethod(CustomerPropertiesTreeUtil.class, "s").getParameterTypes()[0]
                .getComponentType());

    }

}
