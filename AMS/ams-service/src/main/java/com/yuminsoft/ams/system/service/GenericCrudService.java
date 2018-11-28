package com.yuminsoft.ams.system.service;

import com.yuminsoft.ams.system.dao.GenericCrudMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public class GenericCrudService<T, PK extends Serializable> extends BaseService {
    protected GenericCrudMapper<T, PK> mapper;

    public GenericCrudService(GenericCrudMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public T getById(PK id) {
        return mapper.findById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<T> getAll() {
        return mapper.findAll();
    }

    @Transactional
    public int save(T entity) {
        return mapper.insert(entity);
    }

    @Transactional
    public int update(T entity){
        return mapper.update(entity);
    }

    @Transactional
    public void remove(T entity) {
        mapper.delete(entity);
    }

    @Transactional
    public void remove(PK id) {
        mapper.deleteById(id);
    }

    @Transactional
    public void remove(PK... ids) {
        if (ids != null) {
            for (PK id : ids) {
                mapper.deleteById(id);
            }
        }
    }
}
