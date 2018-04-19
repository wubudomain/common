package top.wboost.common.kylin.support.repository.v2.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInvocation;

import top.wboost.common.base.page.PageResolver;
import top.wboost.common.constant.Global;
import top.wboost.common.context.config.AutoProxy;
import top.wboost.common.kylin.api.KylinApi;
import top.wboost.common.kylin.entity.ApiSqlResultEntity;
import top.wboost.common.kylin.search.ApiSqlSearch;
import top.wboost.common.kylin.support.repository.annotation.KylinQuery;
import top.wboost.common.kylin.support.repository.config.ConfigForKylinRepository;
import top.wboost.common.kylin.support.repository.exception.KylinRepositoryNoProjectException;
import top.wboost.common.kylin.support.repository.exception.KylinRepositoryParseSqlException;
import top.wboost.common.kylin.support.repository.util.WildCardUtil;
import top.wboost.common.kylin.support.repository.v2.annotation.KylinRepository;
import top.wboost.common.kylin.util.KylinBusinessUtil;
import top.wboost.common.kylin.util.KylinQueryUtil;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.manager.DefaultAnnotationManager;
import top.wboost.common.sql.fragment.Fragment;
import top.wboost.common.system.exception.SystemException;
import top.wboost.common.util.ClassPathDataUtil;
import top.wboost.common.util.StringUtil;
import top.wboost.common.utils.web.utils.PropertiesUtil;

public class KylinRepositoryProxy implements AutoProxy {

    private Logger log = LoggerUtil.getLogger(getClass());

    public AutoProxy getObject(Class<?> clazz, Map<String, Object> config) throws Exception {
        KylinRepositoryProxy proxy = new KylinRepositoryProxy(clazz, (String) config.get("configPath"));
        return proxy;
    }

    private Map<String, String> sqlMap = new ConcurrentHashMap<String, String>();
    private Class<?> clazz;
    private String project;

    public KylinRepositoryProxy() {
        super();
    }

    public KylinRepositoryProxy(Class<?> clazz, String configPath) {
        super();
        this.clazz = clazz;
        this.project = getProject(clazz);
        log.debug("class:{} load,project is {}", clazz, project);
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String configName = getSqlConfigName(clazz, method);
            KylinQuery kylinQuery = method.getAnnotation(KylinQuery.class);
            String configSql = null;
            if (kylinQuery == null) {
                configSql = PropertiesUtil.getProperty(configName, configPath);
            } else {
                if (StringUtil.notEmpty(kylinQuery.configPath())) {
                    configSql = ClassPathDataUtil.loadData(kylinQuery.configPath());
                } else {
                    configSql = kylinQuery.value();
                }
            }
            if (!StringUtil.notEmpty(configSql)) {
                throw new KylinRepositoryParseSqlException(
                        "cant find sql config:" + configName + " ,for class:" + clazz);
            }
            sqlMap.put(configName, configSql);
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String sql = sqlMap.get(getSqlConfigName(this.clazz, method));
        String realSql = replaceSql(sql, invocation.getArguments());
        ApiSqlSearch sqlSearch = KylinApi.createApiSqlSearch(this.project, realSql);
        Object[] arguments = invocation.getArguments();
        if (arguments != null && arguments.length > 0) {
            if (PageResolver.class.isAssignableFrom(arguments[arguments.length - 1].getClass())) {
                PageResolver pageable = (PageResolver) arguments[arguments.length - 1];
                if (pageable.getBasePage() != null) {
                    sqlSearch.setLimit(pageable.getPageSize());
                    sqlSearch.setOffset(pageable.getOffset());
                } else {
                    log.debug("basePage is null,ignore");
                }
            }
        }
        if (Global.ISDEBUG) {
            if (log.isInfoEnabled())
                log.info("query by sql : {}", sqlSearch.getSql());
        }
        if (realSql == null) {
            throw new KylinRepositoryParseSqlException("sql is null");
        }
        ApiSqlResultEntity resultEntity = KylinQueryUtil.queryByApiSql(sqlSearch);
        Object result = resultEntity.getResults();
        if (method.getReturnType() == ApiSqlResultEntity.class) {
            return resultEntity;
        } else if (method.getReturnType() == String.class) {
            return result.toString();
        } else {
            //FieldsConvert注解生效
            Object resultReturn = DefaultAnnotationManager.resolveFieldsConvert(method, result);
            return resultReturn;
        }
    }

    // 匹配{}
    private static final String PATTERN_COMPILE = "\\{(.*?)\\}";

    @SuppressWarnings("unchecked")
    private String replaceSql(String sql, Object[] params) {
        if (params == null || sql == null)
            return sql;
        try {
            Object[] replaceParams = params;
            if ((replaceParams.length == 1
                    || (replaceParams.length == 2 && PageResolver.class.isAssignableFrom(replaceParams[1].getClass()))
                            && replaceParams[0] instanceof Map)) {
                Map<String, Object> replaceMap = null;
                if (replaceParams.length == 1) {
                    replaceMap = (Map<String, Object>) replaceParams[0];
                } else {
                    replaceMap = (Map<String, Object>) replaceParams[1];
                }
                return WildCardUtil.replace(sql, replaceMap);
            } else {
                List<String> replaceList = StringUtil.getPatternMattcherList(sql, PATTERN_COMPILE, 1);
                for (String replaceIndex : replaceList) {
                    int index = Integer.parseInt(replaceIndex);
                    if (index < replaceParams.length && index >= 0) {
                        if (replaceParams[index] instanceof Fragment) {
                            sql = sql.replaceAll("\\{" + index + "\\}",
                                    ((Fragment) replaceParams[index]).toFragmentString());
                        } else {
                            sql = sql.replaceAll("\\{" + index + "\\}", replaceParams[index].toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(" replace message error ,{}", e.getLocalizedMessage());
        }
        return sql;
    }

    private String getSqlConfigName(Class<?> clazz, Method method) {
        String clazzName = clazz.getSimpleName();
        String realName = clazzName.substring(0, clazzName.indexOf("Repository"));
        realName = Character.toLowerCase(realName.charAt(0)) + realName.substring(1);
        String methodName = method.getName();
        String configName = realName + "-" + methodName;
        return configName;
    }

    private String getProject(Class<?> clazz) {
        KylinRepository kylinRepository = this.clazz.getAnnotation(KylinRepository.class);
        String project = null;
        if (kylinRepository == null) {
            throw new SystemException("kylinRepository is null");
        }
        if ((!StringUtil.notEmpty(kylinRepository.project()) && (!StringUtil.notEmpty(kylinRepository.cubeName())))) {
            project = ConfigForKylinRepository.DEFAULT_PROJECT_NAME;
        } else if (StringUtil.notEmpty(kylinRepository.project())) {
            project = kylinRepository.project();
        } else {
            project = KylinBusinessUtil.queryProjectByCubeName(kylinRepository.cubeName()).getName();
        }
        if (StringUtil.notEmpty(project)) {
            return project;
        }
        throw new KylinRepositoryNoProjectException(clazz.getName());
    }

}