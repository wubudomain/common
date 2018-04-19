package top.wboost.common.utils.web.utils;

/**
 * JSON配置项
 * @className JSONConfig
 * @author jwSun
 * @date 2017年12月28日 下午2:56:01
 * @version 1.0.0
 */
public class JSONConfig {

    /**禁用循环检测,格式化所有,但会有堆栈溢出问题 Stack overflow**/
    private boolean disableCircularReferenceDetect = false;

    public boolean isDisableCircularReferenceDetect() {
        return disableCircularReferenceDetect;
    }

    public void setDisableCircularReferenceDetect(boolean disableCircularReferenceDetect) {
        this.disableCircularReferenceDetect = disableCircularReferenceDetect;
    }

}
