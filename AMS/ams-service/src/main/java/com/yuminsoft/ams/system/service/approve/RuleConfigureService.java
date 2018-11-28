package com.yuminsoft.ams.system.service.approve;

import com.ymkj.pms.biz.api.vo.response.ResGroupVO;
import com.yuminsoft.ams.system.domain.approve.AgenLeader;
import com.yuminsoft.ams.system.domain.approve.ProcessrulesCfg;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.apply.GroupVO;
import com.yuminsoft.ams.system.vo.apply.UserRuleParamIn;
import com.yuminsoft.ams.system.vo.apply.UserRuleVO;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

import java.util.List;

public interface RuleConfigureService {

    /**
     * 规则配置流程列表
     * 组行
     */
    ResponsePage<ProcessrulesCfg> findRuleAndUserList(RequestPage requestPage);

    /**
     * 规则配置
     * 修改保存
     * @param processrulesCfg
     * @return
     */
    Result<String> updateProcessRule(ProcessrulesCfg processrulesCfg);

    /**
     * 代理组长保存
     * @param 
     * @return
     */
    int saveProxyUser(AgenLeader agenLeader);

    /**
     * 查出为该用户分配的产品code列表
     * 
     * @param userCode
     * @return
     * @author JiaCX
     * @date 2017年4月6日 下午3:29:52
     */
    List<String> findAllProductCodeByUserCode(String userCode);

    /**
     * 规则配置，删除配置
     * 
     * @param id
     * @return
     * @author JiaCX
     * @date 2017年4月12日 下午1:38:39
     */
    Result<String> deleteProcessRule(long id);

    /**
     * 重新组装小组信息和组员信息
     * 
     * @param list
     * @return
     * @author JiaCX
     * @date 2017年4月10日 下午2:24:38
     */
    List<GroupVO> combineEmployee(List<ResGroupVO> list);
	
    /**
     * 获取规则配置中被收回权限的用户规则列表
     * 
     * @param requestPage
     * @return
     * @author JiaCX
     * @date 2017年9月4日 下午5:44:59
     */
    ResponsePage<UserRuleVO> getUserRuleList(RequestPage requestPage);

    /**
     * 新增收回权限
     * 
     * @param list
     * @return
     * @author JiaCX
     * @date 2017年9月5日 上午10:14:51
     */
    Result<String> addUserRule(List<UserRuleParamIn> list);

    /**
     * 修改收回权限
     * 
     * @param list
     * @return
     * @author JiaCX
     * @date 2017年9月5日 上午10:20:03
     */
    Result<String> editUserRule(List<UserRuleParamIn> list);
    
    /**
     * 删除某用户被收回的权限
     * 
     * @param id
     * @return
     * @author JiaCX
     * @date 2017年9月5日 上午10:19:28
     */
    Result<String> deleteUserRule(Long id);

    /**
     * 根据用户查询所在的大组小组
     * 
     * @param userCode
     * @return
     * @author JiaCX
     * @date 2017年9月7日 上午10:16:24
     */
//    ResGroupVO findGroupInfoByAccount(String userCode);

    /**
     * 根据userCode获取该用户被收回的权限信息
     * 
     * @param userCode
     * @return
     * @author JiaCX
     * @date 2017年9月7日 上午10:33:43
     */
    List<UserRuleVO> getUserRuleByUserCode(String userCode);

    /**
     * 判断辖下初审员是否均已存在记录
     * 
     * @return
     * @author JiaCX
     * @throws Exception 
     * @date 2017年9月8日 上午10:41:13
     */
    int ifAllCollected() throws Exception;

    /**
     * 获取当前登录人权限内被收回权限的用户id
     * 
     * @return
     * @author JiaCX
     * @date 2017年9月8日 下午12:46:55
     */
    List<String> getAllCollectedUserIds();

    
    

}
