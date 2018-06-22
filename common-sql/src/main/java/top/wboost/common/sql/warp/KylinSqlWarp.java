package top.wboost.common.sql.warp;

import java.util.Date;

import top.wboost.common.utils.web.utils.DateUtil;

public class KylinSqlWarp extends AbstractSqlWarp {

    public String warpDate(Object val) {
        Date date = (Date) val;
        return "to_date(" + DateUtil.format(date) + ",yyyy-mm-dd hh24:mi:ss)";
    }

}
