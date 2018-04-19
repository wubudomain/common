package top.wboost.common.base.service;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.data.domain.Page;

import top.wboost.common.base.enums.QueryType;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.base.repository.BaseRepository;

/**
 * Service公共基础类
 * ClassName: BaseService 
 * @author jwSun
 * @date 2016年7月17日
 */
public interface BaseService<T, ID extends java.io.Serializable> {

    //*************************基础查询功能*************************************//

    /**
     * 新增
     * @author jwSun
     * @date 2017年4月7日 上午11:04:51
     * @param t
     * @return
     */
    public T save(T t);

    /**
     * 更新有值的字段
     * @param id 更新id
     * @param t 更新值
     * @return
     */
    public T update(ID id, T t);

    /**
     * 删除
     * @author jwSun
     * @date 2016年7月17日下午12:04:59
     */
    public boolean delete(@SuppressWarnings("unchecked") ID... ids);

    /**
     * 根据id修改单个属性
     * @author jwSun
     * @date 2016年10月13日下午8:17:47
     * @param id 修改资源id
     * @param key 修改资源key
     * @param value 修改资源value
     */
    public void updateById(ID id, Field key, Object value);

    /**
     * 根据id查询单个数据
     * @param id 查询数据id
     * @return T 查询类
     * @date 2016年7月17日下午12:06:13
     */
    public T findById(ID id);

    /**
     * 根据调传入条件查询List
     * @param t 带查询条件的查询类
     * @param basePage 分页参数
     * @param likeFields 使用like条件查询的属性值
     * @return List<T> 查询结果列表
     */
    public Page<T> findList(T t, QueryPage basePage, String... likeFields);

    /**
     * 根据调传入条件查询List
     * @param t 带查询条件的查询类
     * @param likeFields 使用like条件查询的属性值
     * @return List<T> 查询结果列表
     */
    public Page<T> findList(T t, String... likeFields);

    /**
     * 根据key-value查询
     * @param clazz 查询的类型
     * @param key 查询属性名
     * @param value 查询值
     * @return
     */
    public List<T> findByField(String key, Object value);

    /**
     * 根据key-value查询
     * @param clazz 查询的类型
     * @param key  查询属性名
     * @param value 查询值
     * @param queryType 查询类型(like equal)
     * @return
     */
    @Deprecated
    public List<T> findByField(String key, Object value, QueryType queryType);

    void setRepository(BaseRepository<T, ID> repository);
}
