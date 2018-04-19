package top.wboost.common.base.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable>
        extends BaseRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}