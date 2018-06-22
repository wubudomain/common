package top.wboost.common.kylin.sql;

import top.wboost.common.sql.builder.SqlBuilderAction;
import top.wboost.common.sql.dialect.KylinDialect;
import top.wboost.common.sql.fragment.QueryJoinFragment;
import top.wboost.common.sql.fragment.QuerySelect;

/**
 * sql创建器
 * @className KylinSqlBuilder
 * @author jwSun
 * @date 2017年8月22日 下午7:19:36
 * @version 1.0.0
 */
public class DefaultKylinSqlBuilder {

    public static QuerySelect createQuery() {
        return SqlBuilderAction.createQuery(new KylinDialect());
    }

    public static QueryJoinFragment createFrom(QuerySelect querySelect, String factTable, String factTableAlias) {
        return SqlBuilderAction.createFrom(querySelect, factTable, factTableAlias);
    }

}
