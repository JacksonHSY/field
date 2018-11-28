package com.yuminsoft.ams.system.dao.quality;

import com.yuminsoft.ams.system.vo.quality.QualityControlDeskVo;

import java.util.List;
import java.util.Map;

public interface QualityControlDeskMapper {
	/**
     * 质检工作台待处理列表
     * @param vo
     * @return
     */
    public List<QualityControlDeskVo> findToDo(QualityControlDeskVo vo);
    /**
     * 质检工作台已完成列表
     * @param vo
     * @return
     */
    public List<QualityControlDeskVo> findDone(QualityControlDeskVo vo);
    /**
     * 手工分派列表
     * @param vo
     * @return
     */
    public List<QualityControlDeskVo> findManualDisPatch(QualityControlDeskVo vo);
    /**
     * 手工改派列表
     * @param vo
     * @return
     */
    public List<QualityControlDeskVo> findManualReform(QualityControlDeskVo vo);
    /**
     * 质检查询列表
     * @param vo
     * @return
     */
    public List<QualityControlDeskVo> findQualityQuery(QualityControlDeskVo vo);
    
    /**
     * 更新
     */
    int update(QualityControlDeskVo vo);
    /**
     * 查询待处理
     */
    public List<QualityControlDeskVo> findToDoByUser(Map<String, Object> map);
    /**
     * 查询待分派件信息
     * @param map
     * @return
     */
    public List<QualityControlDeskVo> findForAssign(Map<String, Object> map);

    /**
     * @Desc: 查询质检处理情况待处理页面数据
     * @Author: phb
     * @Date: 2017/5/10 10:34
     */
    public List<QualityControlDeskVo> findInspectionTodoList(Map<String, Object> param);

    /**
     * @Desc: 查询质检处理情况已完成页面数据
     * @Author: phb
     * @Date: 2017/5/10 10:34
     */
    public List<QualityControlDeskVo> findInspectionDoneList(Map<String, Object> param);
    /**
     * 质检信息待质检查询
     */
    public List<QualityControlDeskVo> findQualityByUser(Map<String, Object> map);
    /**
     * 质检信息已质检查询
     */
    public List<QualityControlDeskVo> findQualityDoneByUser(Map<String, Object> map);

}