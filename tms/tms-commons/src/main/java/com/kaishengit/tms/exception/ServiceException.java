package com.kaishengit.tms.exception;

/*
 *业务出现的异常
 * @author 马得草
 * @date 2018/4/12
 */
public class ServiceException extends RuntimeException {

    public ServiceException(){}

    public ServiceException(Throwable th) {
        super(th);
    }

    public ServiceException(String message,Throwable th) {
        super(message,th);
    }

    public ServiceException(String message) {
        super(message);
    }

}
