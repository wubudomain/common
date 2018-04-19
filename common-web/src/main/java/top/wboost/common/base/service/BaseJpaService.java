package top.wboost.common.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import top.wboost.common.base.page.QueryPage;

/**
 * 使用JPA Service公共基础类
 * @className BaseJpaService
 * @author jwSun
 * @date 2017年6月28日 下午8:10:54
 * @version 1.0.0
 * @param <T>
 */
public interface BaseJpaService<T, ID extends java.io.Serializable> extends BaseService<T, ID> {

    /**
     * 根据调传入条件查询List
     * @param Specification specification
     */
    public Page<T> findList(Specification<T> spec);

    /**
     * 根据调传入条件查询List
     * @param Specification specification
     * @param basePage 分页参数
     */
    public Page<T> findList(Specification<T> spec, QueryPage basePage);

    //**************************自定义功能*************************************//

}
