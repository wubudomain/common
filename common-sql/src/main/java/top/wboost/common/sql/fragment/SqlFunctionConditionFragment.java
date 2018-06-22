package top.wboost.common.sql.fragment;

import top.wboost.common.sql.dialect.SqlWarp;
import top.wboost.common.sql.enums.SqlFunction;

public class SqlFunctionConditionFragment implements Fragment {
    private String tableAlias;
    private String op = "=";
    private SqlFunction sqlFunction;
    private String column;
    private SqlWarp sqlWarp;
    private Object value;

    public SqlFunctionConditionFragment(SqlWarp sqlWarp) {
        super();
        this.sqlWarp = sqlWarp;
    }

    public SqlFunctionConditionFragment() {
        super();
        this.sqlWarp = SqlWarp.defaultSqlWarp;
    }

    /**
     * Sets the op.
     * @param op The op to set
     */
    public SqlFunctionConditionFragment setOp(String op) {
        this.op = op;
        return this;
    }

    public SqlFunctionConditionFragment setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
        return this;
    }

    public SqlFunctionConditionFragment setSqlFunction(String column, SqlFunction sqlFunction, Object value) {
        this.column = column;
        this.sqlFunction = sqlFunction;
        this.value = value;
        return this;
    }

    public String toFragmentString() {
        String valuewarp = null;
        if (value instanceof String) {
            if (((String) value).endsWith(":nowarp")) {
                valuewarp = value.toString().substring(0, value.toString().length() - 7);
            }
        }
        if (valuewarp == null) {
            valuewarp = sqlWarp.warp(value);
        }
        return sqlFunction.getSqlFunctionFragment().toFragmentString(null, tableAlias + "." + column) + " " + this.op
                + " " + valuewarp;
    }

}