package com.kaishengit.tms.exception;

/*
 *ҵ����ֵ��쳣
 * @author ��ò�
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
