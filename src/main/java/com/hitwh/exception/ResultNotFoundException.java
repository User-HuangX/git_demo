package com.hitwh.exception;

/**
 * ResultNotFoundException类用于表示结果未找到的异常
 * 它是AppException的一个特化版本，专注于处理资源未找到的情况
 */
public class ResultNotFoundException extends AppException {
    /**
     * 构造函数用于创建ResultNotFoundException实例
     *
     * @param message 异常的详细信息，描述了发生异常的具体情况
     */
    public ResultNotFoundException(String message) {
        super(message, 404); // 404表示资源未找到
    }
}
