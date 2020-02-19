package com.ltyc.sms.common;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class NotSupportedException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7588016798736480605L;

    public NotSupportedException(String message) {
        super(message);
    }
}
