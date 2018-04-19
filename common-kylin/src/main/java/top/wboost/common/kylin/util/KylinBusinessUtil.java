package top.wboost.common.kylin.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import top.wboost.common.kylin.api.KylinApi;
import top.wboost.common.kylin.entity.ApiSqlResultEntity;
import top.wboost.common.kylin.entity.ProjectsReadable;
import top.wboost.common.kylin.entity.ProjectsReadable.Realizations;
import top.wboost.common.kylin.entity.TablesAndColumns;
import top.wboost.common.kylin.entity.TablesAndColumns.Column;
import top.wboost.common.kylin.exception.KylinNoTableException;
import top.wboost.common.kylin.search.ApiSqlSearch;
import top.wboost.common.kylin.sql.DefaultKylinSqlBuilder;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.sql.enums.SqlFunction;
import top.wboost.common.sql.fragment.Fragment;
import top.wboost.common.sql.fragment.QueryJoinFragment;
import top.wboost.common.sql.fragment.QuerySelect;
import top.wboost.common.util.Checker;
import top.wboost.common.util.CollectionUtil;

/**
 * kylin 与业务相关工具类
 * @className KylinBusinessUtil
 * @author jwSun
 * @date 2017年8月22日 下午7:20:01
 * @version 1.0.0
 */
public class KylinBusinessUtil {

    private static Logger log = LoggerUtil.getLogger(KylinBusinessUtil.class);

    /**
     * 根据cube名查询project
     * @param cubeName cube名
     * @return {@link top.wboost.common.kylin.entity.ProjectsReadable}
     */
    public static ProjectsReadable queryProjectByCubeName(String cubeName) {
        List<ProjectsReadable> projectsReadableList = KylinQueryUtil
                .queryList(KylinApi.createApiProjectsReadableSearch(), ProjectsReadable.class);
        Checker.hasRecord(projectsReadableList);
        for (ProjectsReadable projectsReadable : projectsReadableList) {
            List<Realizations> realizationsList = projectsReadable.getRealizations();
            if (!CollectionUtil.isEmpty(realizationsList)) {
                for (Realizations realizations : realizationsList) {
                    if (realizations.getType().equals("CUBE") && realizations.getRealization().equals(cubeName)) {
                        return projectsReadable;
                    }
                }
            }
        }
        log.debug("not find project by cubeName : {}", cubeName);
        return null;
    }

    /**
     * 查询字段详细信息
     * @param cubeName cube名
     * @param columnName 字段名
     * @param tableName 表名
     * @return {@link top.wboost.common.kylin.entity.TablesAndColumns.Column}
     */
    public static Column queryColumnDetailByCubeNameAndColumnNameAndTableName(String cubeName, String columnName,
            String tableName) {
        List<TablesAndColumns> tablesAndColumnsList = KylinQueryUtil
                .queryList(KylinApi.createApiTablesAndColumnsSearch().setCubeName(cubeName), TablesAndColumns.class);
        if (tablesAndColumnsList.size() == 0) {
            throw new KylinNoTableException("tableName:" + tableName + ",columnName:" + columnName);
        }
        for (TablesAndColumns tablesAndColumns : tablesAndColumnsList) {
            if (tablesAndColumns.getTableName().equals(TableUtil.getTableName(tableName))) {
                for (Column column : tablesAndColumns.getColumns()) {
                    if (column.getColumnName().equals(columnName)) {
                        return column;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查询去重值
     * @param cubeName cube名
     * @param tableName 表名
     * @param column 字段名
     * @param whereFragments 过滤条件
     * @return 值数组
     */
    public static Object[] queryDistinct(String cubeName, String tableName, String column, Fragment... whereFragments) {
        QuerySelect querySelect = getDistinctSelect(tableName, column);
        for (Fragment conditions : whereFragments) {
            querySelect.prependWhereConditions(conditions);
        }
        ProjectsReadable projectsReadable = queryProjectByCubeName(cubeName);
        Checker.hasRecord(projectsReadable);
        String sql = querySelect.toRealQueryString();
        return executeDistinctSelect(projectsReadable.getName(), sql);
    }

    /**
     * 函数查询去重值
     * @param cubeName cube名
     * @param tableName 表名
     * @param functionAlias 函数结果别名
     * @param sqlFunction 函数 {@link top.wboost.common.sql.enums.SqlFunction}
     * @param sqlFunctionFragmentParams 函数所需值
     * @param whereFragments 过滤条件
     * @return 值数组
     */
    public static Object[] queryDistinct(String cubeName, String tableName, String functionAlias,
            SqlFunction sqlFunction, Object[] sqlFunctionFragmentParams, Fragment... whereFragments) {
        QuerySelect querySelect = getDistinctSelect(tableName, functionAlias, sqlFunction, sqlFunctionFragmentParams);
        for (Fragment conditions : whereFragments) {
            querySelect.prependWhereConditions(conditions);
        }
        ProjectsReadable projectsReadable = queryProjectByCubeName(cubeName);
        Checker.hasRecord(projectsReadable);
        String sql = querySelect.toRealQueryString();
        return executeDistinctSelect(projectsReadable.getName(), sql);
    }

    private static QuerySelect getDistinctSelect(String tableName, String column) {
        QuerySelect querySelect = DefaultKylinSqlBuilder.createQuery();
        querySelect.addSelectColumn(tableName, column);
        querySelect.setDistinct(true);
        querySelect.setOrderByToken(tableName, column);
        QueryJoinFragment join = DefaultKylinSqlBuilder.createFrom(querySelect, tableName, tableName);
        querySelect.setJoinFragment(join);
        return querySelect;
    }

    private static QuerySelect getDistinctSelect(String tableName, String functionAlias, SqlFunction sqlFunction,
            Object... objects) {
        QuerySelect querySelect = DefaultKylinSqlBuilder.createQuery();
        querySelect
                .addSelectFragmentString(sqlFunction.getSqlFunctionFragment().toFragmentString(functionAlias, objects));
        querySelect.setDistinct(true);
        QueryJoinFragment join = DefaultKylinSqlBuilder.createFrom(querySelect, tableName, tableName);
        querySelect.setJoinFragment(join);
        return querySelect;
    }

    private static Object[] executeDistinctSelect(String projectName, String sql) {
        ApiSqlSearch sqlSearch = KylinApi.createApiSqlSearch(projectName, sql);
        log.debug("distinct sql:{}", sqlSearch.getSql());
        ApiSqlResultEntity result = KylinQueryUtil.queryByApiSql(sqlSearch);
        Object[] retArray = new Object[result.getResults().size()];
        for (int i = 0; i < retArray.length; i++) {
            JSONArray resultOne = (JSONArray) result.getResults().get(i);
            retArray[i] = resultOne.get(0);
        }
        return retArray;
    }

}
