package com.yuminsoft.ams.system.exception;

/**
 * 没有邮件接收人异常
 *@author wulj
 */
public class RecipientNotFoundException extends Exception {

    public RecipientNotFoundException(String message) {
        super(message);
    }

    public RecipientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipientNotFoundException(Throwable cause) {
        super(cause);
    }
}
