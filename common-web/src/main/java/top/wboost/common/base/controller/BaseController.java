package top.wboost.common.base.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.log.util.LoggerUtil;

/**
 * Controller公共基础类
 * ClassName: BaseController 
 * @author jwSun
 * @date 2016年7月17日
 */
public class BaseController {

    protected Logger log = LoggerUtil.getLogger(getClass());

    //参数绑定编辑器
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        /*binder.registerCustomEditor(int.class, new MyEditor());
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(Date.class, new DateEditor());
        binder.registerCustomEditor(Timestamp.class, new TimestampEditor());*/
    }

    public static ResultEntity getResolveEntity(int businessCode) {
        return ResultEntity.success(businessCode).build();
    }

}
