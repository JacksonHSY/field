package com.yuminsoft.ams.system.dao;

import java.util.List;

/**
 * myBatis generic mapper
 * @author wulj
 * @param <T>
 * @param <PK>
 */
public interface GenericCrudMapper<T, PK> {

    T findById(PK id);

    List<T> findAll();

    int insert(T o);

    int update(T o);

    int delete(T o);

    int deleteById(PK id);
}
