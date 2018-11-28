package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.domain.quality.QualitySetInfo;

import java.util.List;
import java.util.Map;

public interface QualitySetInfoMapper {
    
    public int save(QualitySetInfo QualitySetInfo);
	
	public int delete(Long id);
    
    public int update(QualitySetInfo QualitySetInfo);
    
    public QualitySetInfo findById(Long id);
    
    public QualitySetInfo findOne(Map<String, Object> map);
    
    public List<QualitySetInfo> findAll(Map<String, Object> map);
    
    public QualitySetInfo findByDate(Map<String, Object> map);
    
    public QualitySetInfo findNearlyDate(Map<String,Object> map);
    
    public List<QualitySetInfo> find(QualitySetInfo QualitySetInfo);
    
    public QualitySetInfo findHand(QualitySetInfo QualitySetInfo);

    QualitySetInfo findLastDate(Map<String, Object> map);
    
    /**
     * 根据下一周期的开始日期获取上一周期
     */
    public QualitySetInfo getLast(Map<String,Object> map);

    int updateSetInfoNotUse(Long id); //设置抽检周期为无效
    List<QualitySetInfo> findPeriodByStartOrEndDate(Map<String,Object> map);
}