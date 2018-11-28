package com.yuminsoft.ams.system.exception;

/**
 * 终审派单异常
 * Created by wulinjie on 2017/6/9.
 */
public class FinalDispatchException extends Exception {

    public FinalDispatchException(String message) {
        super(message);
    }

    public FinalDispatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinalDispatchException(Throwable cause) {
        super(cause);
    }



}
