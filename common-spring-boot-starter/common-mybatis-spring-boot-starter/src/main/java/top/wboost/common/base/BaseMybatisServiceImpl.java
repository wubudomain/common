package top.wboost.common.base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import top.wboost.common.base.enums.QueryType;
import top.wboost.common.base.page.BasePage;
import top.wboost.common.base.page.QueryPage;
import top.wboost.common.base.repository.BaseRepository;
import top.wboost.common.base.service.BaseService;
import top.wboost.common.base.service.impl.BaseServiceImpl;
import top.wboost.common.system.code.SystemCode;
import top.wboost.common.system.exception.SystemCodeException;
import top.wboost.common.util.ReflectUtil;

public class BaseMybatisServiceImpl<T, Mapper extends tk.mybatis.mapper.common.Mapper<T>, ID extends java.io.Serializable>
        extends BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    @Autowired
    protected Mapper mapper;

    @Override
    public T save(T t) {
        mapper.insertSelective(t);
        return t;
    }

    @Override
    public T update(ID id, T t) {
        try {
            getSetIdMethod().invoke(t, id);
            mapper.updateByPrimaryKeySelective(t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemCodeException(SystemCode.UPDATE_FAIL);
        }
    }

    @Override
    public boolean delete(ID... ids) {
        for (ID id : ids) {
            mapper.deleteByPrimaryKey(id);
        }
        return true;
    }

    private Method getSetIdMethod() {
        Class<T> clazz = getThisClass();
        Optional<Field> field = Arrays.asList(ReflectUtil.findFields(clazz)).stream().filter(f -> {
            return AnnotationUtils.findAnnotation(f, Id.class) != null;
        }).findFirst();
        if (field.isPresent()) {
            return ReflectUtil.getWriteMethod(clazz, field.get().getName());
        } else {
            return null;
        }
    }

    @Override
    public void updateById(ID id, Field key, Object value) {
        try {
            Class<T> clazz = getThisClass();
            T t = clazz.newInstance();
            getSetIdMethod().invoke(t, id);
            ReflectUtil.getWriteMethod(clazz, key.getName()).invoke(t, value);
            mapper.updateByPrimaryKeySelective(t);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public T findById(ID id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<T> findList(T t, QueryPage basePage, String... likeFields) {
        if (basePage == null) {
            basePage = new QueryPage();
        }
        if (basePage.getBasePage() == null) {
            basePage.setBasePage(new BasePage());
        }
        Example example = null;
        try {
            example = resolveExample(t, likeFields);
        } catch (Exception e) {
            throw new SystemCodeException(SystemCode.PARSE_ERROR, e);
        }
        if (basePage.getBasePage().isAllResult()) {
            long count = mapper.selectCountByExample(example);
            if (count == 0L)
                return new PageImpl<T>(new ArrayList<T>(), basePage.getPageResolver(), count);
            basePage.getBasePage().setAllResultPage(Integer.parseInt(String.valueOf(count)));

        }
        PageHelper.startPage(basePage.getBasePage().getPageNumber(), basePage.getBasePage().getPageSize());
        List<T> list = mapper.selectByExample(example);
        PageInfo<T> info = new PageInfo<T>(list);
        Page<T> page = new PageImpl<T>(info.getList(), basePage.getPageResolver(), info.getTotal());
        return page;
    }

    private Example resolveExample(T t, String... likeFields)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<T> clazz = getThisClass();
        Example example = new Example(clazz);
        Criteria criteria = example.createCriteria();
        Field[] fields = ReflectUtil.findFields(clazz);
        Set<String> likeFieldsCol = null;
        if (likeFields.length > 0) {
            likeFieldsCol = new HashSet<>(Arrays.asList(likeFields));
            for (Field field : Arrays.asList(fields)) {
                String name = field.getName();
                Object val = ReflectUtil.getReadMethod(clazz, name).invoke(t);
                if (val != null) {
                    if (likeFieldsCol.contains(name)) {
                        criteria.andLike(name, "%" + val.toString() + "%");
                    } else {
                        criteria.andEqualTo(name, val.toString());
                    }
                }
            }
        }
        return example;
    }

    @Override
    public Page<T> findList(T t, String... likeFields) {
        try {
            Example example = resolveExample(t, likeFields);
            List<T> list = mapper.selectByExample(example);
            PageInfo<T> info = new PageInfo<T>(list);
            Page<T> page = new PageImpl<T>(info.getList(), new QueryPage().getPageResolver(), info.getTotal());
            return page;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<T> findByField(String key, Object value) {
        try {
            Class<T> clazz = getThisClass();
            T t = clazz.newInstance();
            ReflectUtil.getWriteMethod(clazz, key).invoke(t, value);
            return mapper.select(t);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<T> findByField(String key, Object value, QueryType queryType) {
        throw new SystemCodeException(SystemCode.NO_IMPL);
    }

    @Override
    public void setRepository(BaseRepository<T, ID> repository) {

    }

    protected Mapper getMapper() {
        return this.mapper;
    }

}
