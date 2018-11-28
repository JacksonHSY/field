package com.yuminsoft.ams.system.service.system.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.yuminsoft.ams.system.domain.system.SysParamDefine;
import com.yuminsoft.ams.system.exception.RecipientNotFoundException;
import com.yuminsoft.ams.system.service.system.CommonParamService;
import com.yuminsoft.ams.system.service.system.MailService;
import com.yuminsoft.ams.system.vo.mail.MailVo;
import com.yuminsoft.ams.system.vo.mail.SyncTaskNumberVo;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wulj
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private CommonParamService commonParamService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${ams.admin.email.username}")
    private String defaultSenderAddress;

    @Value("${ams.system.env}")
    private String env;

    @Override
    public MailVo sendFirstDispatchFailedMail(Integer flag, String name, String idNo, String loanNo, String staffName, String staffCode) throws RecipientNotFoundException {
        LOGGER.info("发送初审复议再申请邮件, 客户姓名:{}, 身份证号:{}, 借款编号:{}, 员工姓名:{}, 员工工号:{}", name, idNo, loanNo, staffName, staffCode);

        String templateName = "firstDispatchFailed.ftl";
        String subject = "复议再申请件分派";

        Map<String, Object> params = Maps.newHashMap();
        params.put("name", name);
        params.put("idNo", idNo);
        params.put("loanNo", loanNo);
        params.put("staffName", staffName);
        params.put("staffCode", staffCode);

        if (flag == 0) {
            // 非直通车邮件接收人
            SysParamDefine sysParamDefine1 = commonParamService.getByParamKeyAndParamType("to", "firstDispatchNormal");
            if (sysParamDefine1 == null || StringUtils.isEmpty(sysParamDefine1.getParamValue())) {
                throw new RecipientNotFoundException("复议在申请件["+ loanNo +"]找不到邮件接收人");
            }
            String[] recipients = StringUtils.split(sysParamDefine1.getParamValue(), ",");

            return sendMail(subject, recipients, null, templateName, params);

        } else {
            // 直通车邮件接收人
            SysParamDefine sysParamDefine1 = commonParamService.getByParamKeyAndParamType("to", "firstDispatchDirect");
            if (sysParamDefine1 == null || StringUtils.isEmpty(sysParamDefine1.getParamValue())) {
                throw new RecipientNotFoundException("复议在申请件["+ loanNo +"]找不到邮件接收人");
            }
            String[] recipients = StringUtils.split(sysParamDefine1.getParamValue(), ",");

            // 直通车邮件抄送人
            String[] ccList = new String[]{};
            SysParamDefine sysParamDefine2 = commonParamService.getByParamKeyAndParamType("cc", "firstDispatchDirect");
            if (sysParamDefine2 != null && StringUtils.isNotEmpty(sysParamDefine2.getParamValue())) {
                ccList = StringUtils.split(sysParamDefine2.getParamValue(), ",");
            }

            return sendMail(subject, recipients, ccList, templateName, params);
        }

    }

    @Override
    public MailVo sendFinalDispatchFailedMail(String name, String idNo, String loanNo, String staffName, String staffCode) throws RecipientNotFoundException {
        LOGGER.info("发送终审复议再申请邮件, 客户姓名:{}, 身份证号:{}, 借款编号:{}, 员工姓名:{}, 员工工号:{}", name, idNo, loanNo, staffName, staffCode);

        String templateName = "finalDispatchFailed.ftl";
        String subject = "终审复议再申请件分派";

        Map<String, Object> params = Maps.newHashMap();
        params.put("name", name);
        params.put("idNo", idNo);
        params.put("loanNo", loanNo);
        params.put("staffName", staffName);
        params.put("staffCode", staffCode);

        // 终审邮件接收人
        SysParamDefine sysParamDefine1 = commonParamService.getByParamKeyAndParamType("to", "finalDispatchNormal");
        if (sysParamDefine1 == null || StringUtils.isEmpty(sysParamDefine1.getParamValue())) {
            throw new RecipientNotFoundException("复议在申请件["+ loanNo +"]找不到邮件接收人");
        }
        String[] recipients = StringUtils.split(sysParamDefine1.getParamValue(), ",");

        // 终审邮件抄送人
        SysParamDefine sysParamDefine2 = commonParamService.getByParamKeyAndParamType("cc", "finalDispatchNormal");
        String[] ccList = new String[]{};
        if (sysParamDefine2 != null && StringUtils.isNotEmpty(sysParamDefine2.getParamValue())) {
            ccList = StringUtils.split(sysParamDefine2.getParamValue(), ",");
        }

        return sendMail(subject, recipients, ccList, templateName, params);
    }

    @Override
    public MailVo sendSyncTaskNumMail(List<SyncTaskNumberVo> syncTaskNumberVoList) {
        LOGGER.info("发送员工同步更正提醒, syncTaskNumberVoList:{}", JSONArray.toJSONString(syncTaskNumberVoList));

        String templateName = "SyncTaskNumMsg.ftl";
        String subject = "每天同步员工队列数提醒";

        // 接收人
        SysParamDefine sysParamDefine1 = commonParamService.getByParamKeyAndParamType("to", "syncTaskNum");
        String[] recipients = StringUtils.split(sysParamDefine1.getParamValue(), ",");

        // 抄送人
        SysParamDefine sysParamDefine2 = commonParamService.getByParamKeyAndParamType("cc", "syncTaskNum");
        String[] ccList = new String[]{};
        if (sysParamDefine2 != null && StringUtils.isNotEmpty(sysParamDefine2.getParamValue())) {
            ccList = StringUtils.split(sysParamDefine2.getParamValue(), ",");
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("list", syncTaskNumberVoList);
        params.put("env", env);

        return sendMail(subject, recipients, ccList, templateName, params);
    }

    @Override
    public MailVo sendMail(String subject, String[] recipients, String[] ccList, String template, Map<String, Object> params) {
        MailVo mailVo = new MailVo();
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            // 发送人
            helper.setFrom(defaultSenderAddress);
            // 接收人
            helper.setTo(recipients);
            // 抄送人
            if (ccList != null && ccList.length > 0) {
                helper.setCc(ccList);
            }
            // 邮件标题
            msg.setSubject(subject);
            // 邮件内容
            String content = generateContent(template, params);
            helper.setText(content, true);
            // 开始发送
            mailSender.send(msg);

            // 返回mailVo
            mailVo.setFrom(defaultSenderAddress);
            mailVo.setToList(Arrays.asList(recipients));
            if(ccList != null ){
                mailVo.setCcList(Arrays.asList(ccList));
            }
            mailVo.setContent(content);
            mailVo.setSubject(subject);
        } catch (Exception e) {
            LOGGER.error("发送邮件异常", e);
        }

        return mailVo;
    }

    /**
     * 生成邮件内容
     *
     * @param templateName
     * @param map
     * @return
     * @throws MessagingException
     */
    private String generateContent(final String templateName, final Map<String, Object> map) throws MessagingException {
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            Map<String, Object> context = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
        } catch (IOException e) {
            throw new MessagingException("模板不存在", e);
        } catch (TemplateException e) {
            throw new MessagingException("处理失败", e);
        }
    }

}
