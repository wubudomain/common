package top.wboost.common.base.restful;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.wboost.common.annotation.Explain;
import top.wboost.common.base.annotation.AutoWebApplicationConfig;
import top.wboost.common.base.entity.ResultEntity;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.utils.web.utils.ConvertUtil;
import top.wboost.common.utils.web.utils.HibernateUtil;

@AutoWebApplicationConfig("autoRequestMethodInvoke")
public class AutoRequestMethodInvoke {

    private PathMatcher pathMatcher = new AntPathMatcher();
    /** path : (requestMethod:value)**/
    private Map<String, Map<String, AutoRequestMethod>> autoMehotdMap = new ConcurrentHashMap<>();

    public void addAutoRequestMethod(String path, AutoRequestMethod method) {
        if (this.autoMehotdMap.get(path) == null) {
            this.autoMehotdMap.put(path, new ConcurrentHashMap<>());
        }
        this.autoMehotdMap.get(path).put(method.getRequestMapping().method()[0].toString(), method);

    }

    public Map<String, Map<String, AutoRequestMethod>> getAutoRequestMethod() {
        return this.autoMehotdMap;
    }

    private AutoRequestMethod getRequestMethod(HttpServletRequest request) {
        String url = request.getRequestURI().substring(request.getContextPath().length(),
                request.getRequestURI().length());
        Iterator<Entry<String, Map<String, AutoRequestMethod>>> iterator = this.autoMehotdMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Map<String, AutoRequestMethod>> entry = iterator.next();
            if (pathMatcher.match(entry.getKey(), url)) {
                return entry.getValue().get(request.getMethod().toUpperCase());
            }
        }
        return null;
    }

    /**
     * 快速根据id查询
     * @param id 查询id
     * @param request
     * @return
     */
    @ResponseBody
    @Explain(value = "根据id查询数据", systemCode = SystemCode.QUERY_FAIL)
    @RequestMapping(value = "/{inspectName}/{id}", method = RequestMethod.GET)
    public ResultEntity QUERY_BY_ID(@PathVariable("id") String id, HttpServletRequest request) {
        AutoRequestMethod method = getRequestMethod(request);
        Object obj = method.getService().findById(id);
        HibernateUtil.loadLazy(obj);
        return ResultEntity.success(SystemCode.QUERY_OK).setData(obj).build();
    }

    public String[] constantArray = new String[] {};

    /**
     * 快速根据参数查询列表
     * @param map 参数
     * @param request
     * @return
     */
    @ResponseBody
    @Explain(value = "列表查询", systemCode = SystemCode.QUERY_FAIL)
    @RequestMapping(value = "/{inspectName}", method = RequestMethod.GET)
    public ResultEntity QUERY_LIST(@RequestParam Map<String, Object> map, HttpServletRequest request, QueryPage page) {
        AutoRequestMethod method = getRequestMethod(request);
        Object converterObj = ConvertUtil.mapConvertToBean(map, method.getEntityClass());
        Page<Object> pageFind = method.getService().findList(converterObj, page,
                method.getEnableBaseRestful().likeFields());
        return ResultEntity.success(SystemCode.QUERY_OK).setPage(pageFind).build();
    }

    /**
     * 快速根据id删除
     * @param id
     * @param request
     * @return
     */
    @ResponseBody
    @Explain(value = "根据id删除数据", systemCode = SystemCode.DEL_FAIL)
    @RequestMapping(value = "/{inspectName}/{id}", method = RequestMethod.DELETE)
    public ResultEntity DELETE_BY_ID(@PathVariable("id") String id, HttpServletRequest request) {
        AutoRequestMethod method = getRequestMethod(request);
        boolean status = method.getService().delete(id);
        return ResultEntity.success(SystemCode.DEL_OK).setData(status).build();
    }

    /**
     * 保存数据
     * @param request
     * @return
     */
    @ResponseBody
    @Explain(value = "保存数据", systemCode = SystemCode.ADD_FAIL)
    @RequestMapping(value = "/{inspectName}", method = RequestMethod.POST)
    public ResultEntity SAVE(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        AutoRequestMethod method = getRequestMethod(request);
        Object converterObj = ConvertUtil.mapConvertToBean(map, method.getEntityClass());
        Object saveEntity = method.getService().save(converterObj);
        return ResultEntity.success(SystemCode.ADD_OK).setData(saveEntity).build();
    }

    /**
     * 更新数据
     * @param request
     * @return
     */
    @ResponseBody
    @Explain(value = "更新数据", systemCode = SystemCode.UPDATE_FAIL)
    @RequestMapping(value = "/{inspectName}/{id}", method = RequestMethod.PUT)
    public ResultEntity UPDATE_BY_ID(@PathVariable("id") String id, @RequestParam Map<String, Object> map,
            HttpServletRequest request) {
        AutoRequestMethod method = getRequestMethod(request);
        Object converterObj = ConvertUtil.mapConvertToBean(map, method.getEntityClass());
        Object saveEntity = method.getService().update(id, converterObj);
        return ResultEntity.success(SystemCode.UPDATE_OK).setData(saveEntity).build();
    }

}
