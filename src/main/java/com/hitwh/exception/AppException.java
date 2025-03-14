package com.hitwh.exception;

import lombok.Getter;

/**
 * 自定义异常基类，继承自RuntimeException
 * 提供了错误代码和错误消息的封装
 */
@Getter
public class AppException extends RuntimeException {
    private final int errorCode;

    /**
     * 构造方法，初始化异常对象
     *
     * @param message   异常信息，描述发生了什么错误
     * @param errorCode 错误代码，标识具体的错误类型
     */
    public AppException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
