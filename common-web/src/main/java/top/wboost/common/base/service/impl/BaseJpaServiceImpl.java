package top.wboost.common.base.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import top.wboost.common.base.enums.QueryType;
import top.wboost.common.base.page.BasePage;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.base.repository.BaseJpaRepository;
import top.wboost.common.base.repository.BaseRepository;
import top.wboost.common.base.service.BaseJpaService;
import top.wboost.common.util.JpaUtil;
import top.wboost.common.util.ReflectUtil;
import top.wboost.common.utils.web.utils.ConvertUtil;

/**
 * 使用JPA Service公共基础类
 * @className BaseJpaService
 * @author jwSun
 * @date 2017年6月28日 下午8:10:54   
 * @version 1.0.0
 * @param <V>
 * @param <V>
 */
public class BaseJpaServiceImpl<T, K extends BaseJpaRepository<T, ID>, ID extends java.io.Serializable>
        extends BaseServiceImpl<T, ID> implements BaseJpaService<T, ID> {

    public BaseJpaServiceImpl(BaseJpaRepository<T, ID> repository) {
        super();
        this.repository = repository;
    }

    public BaseJpaServiceImpl() {
        super();
    }

    @Override
    public Page<T> findList(T t, QueryPage basePage, String... likeFields) {
        Specification<T> specification = JpaUtil.getSpecification(t, likeFields);
        return findList(specification, basePage);
    }

    /**
     * 根据调传入条件查询List
     * @param t 带查询条件的查询类
     * @param likeFields 使用like条件查询的属性值
     * @return List<T> 查询结果列表
     */
    public Page<T> findList(T t, String... likeFields) {
        Specification<T> specification = JpaUtil.getSpecification(t, likeFields);
        return findList(specification, null);
    }

    /**
     * 根据调传入条件查询List
     * @param Specification specification
     */
    public Page<T> findList(Specification<T> spec) {
        return findList(spec, null);
    }

    public Page<T> findList(Specification<T> spec, QueryPage basePage) {
        if (basePage == null) {
            basePage = new QueryPage();
        }
        if (basePage.getBasePage() == null) {
            basePage.setBasePage(new BasePage());
        }
        if (basePage.getBasePage().isAllResult()) {
            long count = getRepository().count(spec);
            if (count == 0L)
                return new PageImpl<T>(new ArrayList<T>(), basePage.getPageResolver(), count);
            basePage.getBasePage().setAllResultPage(Integer.parseInt(String.valueOf(count)));
        }
        return getRepository().findAll(spec, basePage.getPageResolver());
    }

    @Override
    public List<T> findByField(String key, Object value) {
        T obj;
        try {
            Class<T> clazz = getThisClass();
            obj = clazz.newInstance();
            ReflectUtil.getWriteMethod(clazz, key).invoke(obj, value);
            Specification<T> specification = JpaUtil.getSpecification(obj);
            List<T> list = getRepository().findAll(specification);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> findByField(String key, Object value, QueryType queryType) {
        try {
            Class<T> clazz = getThisClass();
            T obj = clazz.newInstance();
            ReflectUtil.getWriteMethod(clazz, key).invoke(obj, value);
            Specification<T> specification = null;
            if (queryType == QueryType.LIKE) {
                specification = JpaUtil.getSpecification(obj, new String[] { key });
            } else {
                specification = JpaUtil.getSpecification(obj);
            }
            List<T> list = getRepository().findAll(specification);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected BaseJpaRepository<T, ID> repository;

    private BaseJpaRepository<T, ID> getBaseRepository() {
        return this.repository;
    }

    @SuppressWarnings("unchecked")
    protected K getRepository() {
        return (K) getBaseRepository();
    }

    /**
     * 根据id修改单个属性
     * @date 2016年10月13日下午8:17:47
     * @param id
     *            修改资源id
     * @param key
     *            修改资源key
     * @param value
     *            修改资源value
     */
    public void updateById(ID id, Field key, Object value) {
        if (key == null) {
            throw new RuntimeException("NO FIELD!");
        }
        T t = getBaseRepository().getOne(id);
        if (t == null) {
            log.debug("method:updateById entity is undefined");
            return;
        }
        try {
            ReflectUtil.getWriteMethod(t.getClass(), key.getName()).invoke(t, value);
            getRepository().save(t);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 根据id查询单个数据
     * @param id 查询数据id
     * @return T 查询类
     * @date 2016年7月17日下午12:06:13
     */
    public T findById(ID id) {
        return getBaseRepository().findOne(id);
    }

    @Override
    public boolean delete(@SuppressWarnings("unchecked") ID... ids) {
        for (ID id : ids) {
            getBaseRepository().delete(id);
        }
        return true;
    }

    @Override
    public T save(T t) {
        return getRepository().save(t);
    }

    @Override
    public T update(ID id, T t) {
        T find = getBaseRepository().getOne(id);
        if (find == null) {
            return find;
        }
        Map<String, Object> fieldMap = ConvertUtil.beanConvertToMap(t);
        fieldMap.forEach((fieldName, value) -> {
            if (value == null) {
                return;
            }
            Method writeMethod = ReflectUtil.getWriteMethod(t.getClass(), fieldName);
            if (writeMethod != null) {
                try {
                    writeMethod.invoke(find, value);
                } catch (Exception e) {
                    log.warn("write field : " + fieldName + " error,cause by : " + e);
                }
            }
        });
        return getRepository().save(find);
    }

    @Override
    public void setRepository(BaseRepository<T, ID> repository) {
        this.repository = (BaseJpaRepository<T, ID>) repository;
    }

}
