package top.wboost.common.sql.dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.RowSelection;

import top.wboost.common.sql.parameter.SqlParameterEncoder;

/**
 * 提供非PreparedStatement sql
 * @className RealSqlDialect
 * @author jwSun
 * @date 2018年6月19日 上午11:23:37
 * @version 1.0.0
 */
public abstract class RealSqlDialect extends Dialect {

    protected SqlParameterEncoder encoder;

    public RealSqlDialect(SqlParameterEncoder encoder) {
        super();
        this.encoder = encoder;
    }

    public abstract String processLimitSql(String sql, RowSelection selection);

}
