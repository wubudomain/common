package top.wboost.common.sql.fragment;

import org.hibernate.internal.util.collections.ArrayHelper;

import top.wboost.common.sql.manager.SqlManager;

public class ConditionFragment implements Fragment {
    private String tableAlias;
    private String[] lhs;
    private String[] rhs;
    private String op = "=";

    public ConditionFragment() {
        super();
    }

    /**
     * Sets the op.
     * @param op The op to set
     */
    public ConditionFragment setOp(String op) {
        this.op = op;
        return this;
    }

    /**
     * Sets the tableAlias.
     * @param tableAlias The tableAlias to set
     */
    public ConditionFragment setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
        return this;
    }

    public ConditionFragment setCondition(Object[] lhs, Object rhs) {
        String[] strArray = new String[lhs.length];
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = lhs[i].toString();
        }
        this.lhs = strArray;
        this.rhs = new String[] { SqlManager.encode(SqlManager.getSqlWarp().warp(rhs)) };
        return this;
    }

    public ConditionFragment setCondition(Object[] lhs, Object[] rhs) {
        String[] strArray = new String[lhs.length];
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = lhs[i].toString();
        }
        this.lhs = strArray;
        this.rhs = SqlManager.encode(SqlManager.getSqlWarp().warp(rhs));
        return this;
    }

    public ConditionFragment setCondition(String[] lhs, String[] rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        return this;
    }

    public ConditionFragment setCondition(String[] lhs, String rhs) {
        this.lhs = lhs;
        this.rhs = ArrayHelper.fillArray(rhs, lhs.length);
        return this;
    }

    public String toFragmentString() {
        StringBuilder buf = new StringBuilder(lhs.length * 10);
        for (int i = 0; i < lhs.length; i++) {
            buf.append(tableAlias).append('.').append(lhs[i]).append(op).append(rhs[i]);
            if (i < lhs.length - 1) {
                buf.append(" and ");
            }
        }
        return buf.toString();
    }

}