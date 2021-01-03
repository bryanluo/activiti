package com.jiang.workflow.common.exception;

/**
 * @author Bryan.luo
 * @CreateBy 2021/1/3 星期日
 * @description
 **/
public class UserFriendlyException extends RuntimeException{

    public UserFriendlyException(){
        super();
    }

    public UserFriendlyException(String  message){
        super(message);
    }

    public UserFriendlyException(String message, Throwable cause){
        super(message, cause);
    }

    public UserFriendlyException(Throwable cause) {
        super(cause);
    }

    protected UserFriendlyException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
