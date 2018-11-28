package com.yuminsoft.ams.system.util;

import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class EmailUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

    /**
     * @param host     邮件服务器
     * @param sender   发件人
     * @param nickName 发件人昵称
     * @param receiver 收件人数组
     * @param userName 用户名
     * @param passWord 密码
     * @param subject  主题
     * @param message  邮件内容
     * @author Jia CX
     * @date 2017年11月15日 下午3:37:40
     * @notes 发送邮件，使用此方法尽可能使用异步发送，因为发送时间【可能】会稍长，为了不影响正常业务逻辑，而且此方法不抛出异常
     */
    public void sendEamil(String host, String sender, String nickName, String[] receiver, String userName, String passWord, String subject, String message) {
        HtmlEmail email = new HtmlEmail();
        try {
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
            email.setHostName(host);
            // 字符编码集的设置
            email.setCharset("UTF-8");
            // 收件人的邮箱
            for (String string : receiver) {
                email.addTo(string);
            }
            // 发送人的邮箱
            email.setFrom(sender, nickName);
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
            email.setAuthentication(userName, passWord);
            // 要发送的邮件主题
            email.setSubject(subject);
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            email.setMsg(message);
            // 发送
            email.send();
            LOGGER.info("接受者为:{}的邮件发送成功", Arrays.toString(receiver));
        } catch (Exception e) {
            LOGGER.error("接受者为:{}的邮件发送失败：", Arrays.toString(receiver), e);
        }

    }

}
