package com.xyz.script.exception;

import lombok.Getter;

/**
 * @author Mohammad Uzair
 * Custom exception class for script-execution-service
 */
@Getter
public class ServiceException extends RuntimeException{


    private static final long serialVersionUID = 7948174494071096432L;

    private ExceptionReason reason;

    public ServiceException(String message){
        super(message);
    }

    /**
     *
     * @param reason
     * @param message
     * Customer exception with message and exception reason
     */
    public ServiceException(String message, ExceptionReason reason){
        this(message);
        this.reason = reason;
    }


}
