package com.hitwh.exception;

/**
 * InvalidRequestException类继承自AppException，用于表示无效的请求异常
 * 这个异常类主要用于处理客户端发送的请求不合法或有误的情况
 */
public class InvalidRequestException extends AppException {
    /**
     * 构造函数，用于创建InvalidRequestException实例
     *
     * @param message 异常信息，描述请求无效的原因
     */
    public InvalidRequestException(String message) {
        super(message, 400); // 400表示坏请求
    }
}
