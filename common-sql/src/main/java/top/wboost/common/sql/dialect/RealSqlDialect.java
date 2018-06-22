package top.wboost.common.sql.dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.RowSelection;

/**
 * 提供非PreparedStatement sql
 * @className RealSqlDialect
 * @author jwSun
 * @date 2018年6月19日 上午11:23:37
 * @version 1.0.0
 */
public abstract class RealSqlDialect extends Dialect {

    public RealSqlDialect() {
        super();
    }

    public abstract String processLimitSql(String sql, RowSelection selection);

}
