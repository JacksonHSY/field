package com.yuminsoft.ams.system.dao.approve;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.approve.AgenLeader;

/**
 * 规则配置
 * @author fusj
 *
 */
public interface AgenLeaderMapper {
    
    public int delete(Long id);
    
    public int save(AgenLeader agenLeader);
    
    public int update(AgenLeader agenLeader);
    
    public AgenLeader findById(Long id);
    
    public AgenLeader findOne(Map<String, Object> map);
    
    public List<AgenLeader> findAll(Map<String, Object> map);
    
    public List<AgenLeader> findByUserCodeList(List<Object> list);
    
    public List<AgenLeader> findByUserCode(String userCode);
}