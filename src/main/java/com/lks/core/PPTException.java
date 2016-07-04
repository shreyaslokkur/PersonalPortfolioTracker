package com.lks.core;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 2/7/16
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PPTException extends RuntimeException {

    private String message;

    public PPTException(String message) {
        super(message);
        this.message = message;
    }

    public PPTException(String message, Throwable cause) {

        super(message, cause);
        this.message = message;
    }
}
