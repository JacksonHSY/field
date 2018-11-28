package com.yuminsoft.ams.system.dao.approve;

import java.util.List;
import java.util.Map;

import com.yuminsoft.ams.system.domain.approve.ProcessrulesCfg;
import com.yuminsoft.ams.system.domain.approve.UserRule;
import com.yuminsoft.ams.system.domain.approve.UserRuleSub;
import com.yuminsoft.ams.system.vo.apply.UserRuleVO;

/**
 * 规则配置
 * @author fusj
 *
 */
public interface ProcessrulesCfgMapper {
    
    public int delete(Long id);
    
    public int save(ProcessrulesCfg processrulesCfg);
    
    public int update(ProcessrulesCfg processrulesCfg);
    
    public ProcessrulesCfg findById(Long id);
    
    public ProcessrulesCfg findOne(Map<String, Object> map);
    
    public List<ProcessrulesCfg> findAll(Map<String, Object> map);
    
    public List<ProcessrulesCfg> findByUserCodeList(List<String> list);
    
    public List<ProcessrulesCfg> findByUserCode(String userCode);

	/**
	 * 校验该规则是否已经存在
	 * 
	 * @param processrulesCfg
	 * @return
	 * @author JiaCX
	 * @date 2017年4月6日 下午4:23:34
	 */
	public boolean hasThisRuleOrNot(ProcessrulesCfg processrulesCfg);

	public List<ProcessrulesCfg> findAllProcessrulesCfg();
	
	/**
	 * 根据审批结果查询规则(用于判断是否需要复核确认)
	 * @author dmz
	 * @date 2017年4月10日
	 * @param map
	 * @return
	 */
	boolean findByApproveResult(Map<String,Object> map);

    /**
     * 删除某用户被收回的权限详细
     * 
     * @param id    被收回权限主表的id
     * @author JiaCX
     * @date 2017年9月5日 上午10:30:50
     */
    public void deleteUserRuleSubByUserRuleId(Long id);

    /**
     * 删除某用户被收回的权限
     * 
     * @param id 被收回权限主表的id
     * @author JiaCX
     * @date 2017年9月5日 上午10:31:14
     */
    public void deleteUserRuleByUserRuleId(Long id);

    /**
     * 插入用户被收回权限主表信息
     * 
     * @param ur
     * @return
     * @author JiaCX
     * @date 2017年9月6日 上午8:41:21
     */
    public int insertUserRule(UserRule ur);

    /**
     * 批量插入用户收回权限详细信息
     * 
     * @param subList
     * @author JiaCX
     * @date 2017年9月6日 上午9:22:10
     */
    public void batchInsertUserRuleSub(List<UserRuleSub> list);

    /**
     * 更新用户被收回权限主表信息
     * 
     * @param ur
     * @author JiaCX
     * @date 2017年9月6日 上午10:34:22
     */
    public void updateUserRule(UserRule ur);

    /**
     * 获取规则配置中被收回权限的用户规则列表
     * 
     * @return
     * @author JiaCX
     * @param userCodeList 查询条件-用户code列表
     * @date 2017年9月6日 下午12:02:38
     */
    public List<UserRuleVO> getUserRuleList(List<String> list);

    /**
     * 获取被收回权限的userCodes
     * 
     * @return
     * @author JiaCX
     * @date 2017年9月8日 上午11:20:57
     */
    public List<String> getTokenBackUsers();
}