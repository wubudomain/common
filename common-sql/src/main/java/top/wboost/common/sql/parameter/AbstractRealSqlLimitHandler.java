package top.wboost.common.sql.parameter;

import org.hibernate.dialect.pagination.AbstractLimitHandler;

public abstract class AbstractRealSqlLimitHandler extends AbstractLimitHandler {

    protected SqlParameterEncoder encoder;

    public AbstractRealSqlLimitHandler(SqlParameterEncoder encoder) {
        super();
        this.encoder = encoder;
    }

}
