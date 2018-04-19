package top.wboost.common.base.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> {

    //public Page<T> findAll();

}