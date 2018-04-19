package top.wboost.common.system.code;

import top.wboost.common.system.code.resolver.CodeResolver;
import top.wboost.common.system.code.resolver.impl.NoPageResolver;
import top.wboost.common.system.code.resolver.impl.NothingCodeResolver;
import top.wboost.common.system.code.resolver.impl.PromptAuthCodeResolver;
import top.wboost.common.system.code.resolver.impl.PromptCodeResolver;
import top.wboost.common.system.code.resolver.impl.RedirectLoginCodeResolver;
import top.wboost.common.system.code.resolver.impl.RestLoginCodeResolver;
import top.wboost.common.system.code.resolver.impl.RestLogoutCodeResolver;
import top.wboost.common.system.code.resolver.impl.SimpleMultipartCodeResolver;

/**
 * 业务代码枚举类
 * @className BusinessCode
 * @author jwSun
 * @date 2017年6月26日 下午8:14:42
 * @version 1.0.0
 */
public enum SystemCode {

    /**重定向至登录界面**/
    REDIRECT_LOGIN(10999, new RedirectLoginCodeResolver(10999)),
    /**提示至登录界面**/
    REST_LOGIN(10989, new RestLoginCodeResolver(10989)),
    /**REST退出**/
    REST_LOGOUT(10988, new RestLogoutCodeResolver(10988)),
    /**提示权限不足**/
    PROMPT_AUTH(10998, new PromptAuthCodeResolver(10998)),
    /**系统级别错误**/
    ERROR(10997),
    /**没有参数**/
    NO_PARAM(10901),
    /**空**/
    NULL(10902),
    /**成功**/
    SUCCESS(10903),
    /**失败**/
    FAIL(10904),
    /**没有此页**/
    NO_PAGE(10905, new NoPageResolver(10905)),
    /**执行成功**/
    DO_OK(10906),
    /**执行失败**/
    DO_FAIL(10907),
    /**验证成功**/
    CHECK_OK(10996),
    /**验证失败**/
    CHECK_FAIL(10995),
    /**没有查询结果**/
    NO_RECORD(10908),
    /**没有EXPLAIN注解**/
    NO_EXPLAIN(10909),
    /**JPA HANDLER 通配符转换失败**/
    WBOOST_HANDLER_BASEPACKAGE_RESOLVE_ERROR(10910),
    /**连接接口失败**/
    CONNECTION_ERROR(10911),
    /**分页转换解析失败**/
    PAGE_RESOLVER_ERROR(10912),
    /**返回原样数据**/
    PROMPT(10913, new PromptCodeResolver()),
    /**解析失败**/
    PARSE_ERROR(10914),
    /**方法还未实现**/
    NO_IMPL(10915),
    /**无含义**/
    NOTHING(0, new NothingCodeResolver()),
    /**查询成功**/
    QUERY_OK(10800),
    /**查询失败**/
    QUERY_FAIL(10820),
    /**增加成功**/
    ADD_OK(10801),
    /**增加失败**/
    ADD_FAIL(10821),
    /**删除成功**/
    DEL_OK(10802),
    /**删除失败**/
    DEL_FAIL(10822),
    /**保存成功**/
    UPDATE_OK(10803),
    /**保存失败**/
    UPDATE_FAIL(10823);

    private CodeResolver codeResolver;
    public int code;

    private SystemCode(int code, CodeResolver codeResolver) {
        this.codeResolver = codeResolver;
        this.code = code;
    }

    private SystemCode(int code) {
        this.code = code;
        this.codeResolver = new SimpleMultipartCodeResolver();
    }

    public CodeResolver getCodeResolver() {
        return this.codeResolver;
    }

    public int getCode() {
        return this.code;
    }
}
