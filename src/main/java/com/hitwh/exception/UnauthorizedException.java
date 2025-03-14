package com.hitwh.exception;

public class UnauthorizedException extends AppException {

    public UnauthorizedException(String message) {
        super(message,401);// 401表示登录未认证
    }
}
