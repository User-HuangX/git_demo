package com.hitwh.controller;

import com.hitwh.exception.InvalidRequestException;
import com.hitwh.model.UserModel;
import com.hitwh.request.LoginRequest;
import com.hitwh.service.LoginService;
import lombok.RequiredArgsConstructor;
import com.hitwh.response.Response;
import com.hitwh.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注册控制器，处理与用户注册相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LoginController {
    private final LoginService loginService;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 处理用户登录请求
     *
     * @param loginRequest 登录信息，包括用户名和密码
     * @return 响应结果，包括登录是否成功和JWT令牌
     */
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        // 记录学生登录信息
        log.info("学生登录:{}", loginRequest);
        // 调用认证服务进行登录验证
        UserModel e = loginService.login(loginRequest);
        // 如果登录成功，生成JWT令牌
        if (e != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", e.getUserId());
            claims.put("roles", e.getRole());
            claims.put("userName", e.getUserName());
            // 生成JWT令牌
            String jwt = JwtUtils.generateJwt(claims);
            String redisKey = "jwt:" + e.getUserId();
            stringRedisTemplate.opsForValue().set(redisKey, jwt, 3600); // 1小时过期
            log.info("登入的JWT令牌为:{}",jwt);
            //返回登录成功及JWT令牌
            return Response.success("登陆成功",jwt);
        }
        // 如果登录失败，抛出异常
        throw new InvalidRequestException("登录失败，请重试");
    }

}
