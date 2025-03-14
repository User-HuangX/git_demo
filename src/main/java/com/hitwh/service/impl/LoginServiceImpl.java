package com.hitwh.service.impl;

import com.hitwh.mapper.UserMapper;
import com.hitwh.model.UserModel;
import com.hitwh.request.LoginRequest;
import com.hitwh.service.LoginService;
import com.hitwh.utils.MD5Encryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LoginServiceImpl implements LoginService {
    private final UserMapper userMapper;
    /**
     * 用户登录
     * @param loginRequest 登录模型，包含用户ID，手机号和密码
     * @return 登录成功的用户信息，如果登录失败则返回null
     */
    @Override
    public UserModel login(LoginRequest loginRequest) {
        // 对密码进行MD5加密
        loginRequest.setPassword(MD5Encryptor.encryptToMD5(loginRequest.getPassword()));
        // 调用Mapper方法进行登录验证
        return userMapper.getUserByPhoneOrUserIdAndPassword(loginRequest);
    }
}
