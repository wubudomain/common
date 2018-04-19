/**
 * 
 */
package top.wboost.common.constant;

import top.wboost.common.base.ConfigForBase;
import top.wboost.common.utils.web.utils.PropertiesUtil;

/**
 * @ClassName: Constant
 * @author jwSun
 * @date 2016年7月20日 上午9:26:15
 */
public class Global {

    //private static Logger log = LoggerUtil.getLogger(Constant.class);

    /*public static HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
    .getRequestAttributes()).getRequest();
    
    
    public static final String Content_Path = request.getServerName() + ":"
    + request.getServerPort() + request.getContextPath();*/

    public final static boolean ISDEBUG = Boolean
            .parseBoolean(PropertiesUtil.getProperty(ConfigForBase.PropertiesConfig.IS_DEBUG));

    /**
     * JSON数据验证
     * @ClassName: HTMLVALIDATE
     * @author sjw
     * @date 2016年7月20日 上午9:28:55
     */
    public interface HTMLVALIDATE {
        public static String SUCCESS = "success";
        public static String MSG = "msg";
        public static String INFO = "info";
        public static String ROWS = "rows";
        public static String DATA = "data";
        public static String PAGE = "page";
        public static String TOTAL = "total";
        public static String CODE = "code";
        public static String VALIDATE = "validate";

        public interface VALIDATE {
            public static String TRUE = "true";
            public static String FALSE = "false";
        }
    }
}
