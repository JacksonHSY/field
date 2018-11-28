package com.yuminsoft.ams.system.service.system;

import com.yuminsoft.ams.system.exception.RecipientNotFoundException;
import com.yuminsoft.ams.system.vo.mail.MailVo;
import com.yuminsoft.ams.system.vo.mail.SyncTaskNumberVo;

import java.util.List;
import java.util.Map;

/**
 * 邮件服务
 * @author wulj
 */
public interface MailService {

    /**
     * 复议再申请初审派单失败邮件提醒
     * @param flag       0-非直通车,1-直通车
     * @param name       客户姓名
     * @param idNo       客户身份证号
     * @param loanNo     申请件编号
     * @param staffName  初审员姓名
     * @param staffCode  初审员工号
     * @throws  RecipientNotFoundException
     * @return 邮件VO
     * @author wulj
     */
    MailVo sendFirstDispatchFailedMail(Integer flag, String name, String idNo, String loanNo, String staffName, String staffCode) throws RecipientNotFoundException;

    /**
     * 复议再申请终审派单失败邮件提醒
     * @param name       客户姓名
     * @param idNo       客户身份证号
     * @param loanNo     申请件编号
     * @param staffName  终审员姓名
     * @param staffCode  终审员工号
     * @author wulj
     * @throws  RecipientNotFoundException
     * @return 邮件VO
     */
    MailVo sendFinalDispatchFailedMail(String name, String idNo, String loanNo, String staffName, String staffCode) throws RecipientNotFoundException;

    /**
     * 任务数更正邮件提醒
     * @param syncTaskNumberVoList
     * @author wulj
     * @return
     */
    MailVo sendSyncTaskNumMail(List<SyncTaskNumberVo> syncTaskNumberVoList);

    /**
     * 发送邮件
     * @param subject       邮件标题
     * @param recipients    接收人
     * @param ccList        抄送人
     * @param template      邮件模板
     * @param params        模板参数
     * @author wulj
     * @return 邮件VO
     */
    MailVo sendMail(final String subject, final String[] recipients,final String[] ccList, final String template, final Map<String, Object> params);

}
