package com.hitwh.service;

import com.hitwh.model.UserModel;
import com.hitwh.request.LoginRequest;

public interface LoginService {
    UserModel login(LoginRequest loginRequest);

}
