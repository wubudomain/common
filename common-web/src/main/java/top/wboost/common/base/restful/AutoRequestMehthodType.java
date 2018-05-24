package top.wboost.common.base.restful;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自动生成方法类型
 * @className AutoRequestMehthodType
 * @author jwSun
 * @date 2018年4月14日 上午11:41:13
 * @version 1.0.0
 */
public enum AutoRequestMehthodType {

    /**GET:     /{inspectName}/{id}   根据主键查询 **/
    QUERY_BY_ID,
    /**GET:     /{inspectName}        列表查询**/
    QUERY_LIST,
    /**DELETE:  /{inspectName}        根据主键删除**/
    DELETE_BY_ID,
    /**PUT:     /{inspectName}/{id}   更新数据**/
    UPDATE_BY_ID,
    /**POST:    /{inspectName}        保存数据**/
    SAVE;

    public static Set<String> getAllNames() {
        Set<String> set = new HashSet<>();
        Arrays.asList(AutoRequestMehthodType.values()).forEach(type -> {
            set.add(type.toString());
        });
        return set;

    }

}
