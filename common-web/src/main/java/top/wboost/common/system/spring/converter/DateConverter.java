package top.wboost.common.system.spring.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.utils.DateUtil;

@AutoRootApplicationConfig
public class DateConverter implements Converter<String, Date> {

    private Logger log = LoggerUtil.getLogger(getClass());

    @Override
    public Date convert(String source) {
        if (!StringUtil.notEmpty(source)) {
            return null;
        }
        if (StringUtil.getAllPatternMattcher(source, DateUtil.DATE.PATTERN_YYYY_MM_DD_HH_MM_SS, 0) != null) {
            return DateUtil.parse(source, DateUtil.DATE.YYYY_MM_DD_HH_MM_SS);
        }
        if (StringUtil.getAllPatternMattcher(source, DateUtil.DATE.PATTERN_YYYY_MM_DD_HH_MM, 0) != null) {
            return DateUtil.parse(source, DateUtil.DATE.YYYY_MM_DD_HH_MM);
        }
        if (StringUtil.getAllPatternMattcher(source, DateUtil.DATE.PATTERN_YYYY_MM_DD, 0) != null) {
            return DateUtil.parse(source, DateUtil.DATE.YYYY_MM_DD);
        }
        log.debug("cant convert :" + source);
        return null;
    }

}
