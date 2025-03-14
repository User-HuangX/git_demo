package com.hitwh.config;

import com.alibaba.fastjson.JSONObject;
import com.hitwh.exception.AppException;
import com.hitwh.exception.ForbiddenException;
import com.hitwh.exception.UnauthorizedException;
import com.hitwh.response.Response;
import com.hitwh.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.io.IOException;

/**
 * 登录检查过滤器
 * 用于全局过滤，检查用户是否登录
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LoginCheckFilter implements Filter {
    private final StringRedisTemplate stringRedisTemplate;
    // 用于匹配URL的路径匹配器

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * 执行过滤操作
     * 检查用户是否登录，以及是否有权限访问请求的资源
     *
     * @param servletRequest  Servlet请求对象
     * @param filterChain     过滤链，用于放行请求
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将请求转换为HTTP类型
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        // 获取请求的URL和远程地址
        String url = req.getRequestURI();
        String remoteAddr = req.getRemoteAddr();

        // 记录请求的URL和来源IP
        log.info("请求的URL: {}, 来自 IP: {}", url, remoteAddr);

        // 对于认证请求，直接放行
        if (url.contains("/login") ||
                url.contains("/swagger") ||
                url.contains("/v3/api-docs") ||
                url.contains("/swagger-ui.html") ||
                url.contains("/favicon.ico")) {
            log.info("登录或测试操作, 放行...");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 从请求头中获取JWT令牌
        String token = req.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            // 返回401未授权
            throw new UnauthorizedException("未登录");
        }
        String jwt = token.substring(7); // 去除"Bearer "前缀
        String userId;

            // 解析JWT令牌
            Claims claims = JwtUtils.parseJWT(jwt);
            log.info("JWT解析为：{}",claims.toString());
            // 获取用户角色和ID
            String role = claims.get("roles").toString();
            userId = claims.get("userId").toString();
            req.setAttribute("userId", userId);
            String redisKey = "jwt:" + userId;
            String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
        // 如果请求头中的token为空，返回未登录信息
            if(redisValue==null || redisValue.isEmpty()){
                throw new UnauthorizedException("未登录");
            }
            if (!stringRedisTemplate.hasKey(redisKey) || !stringRedisTemplate.opsForValue().get(redisKey).trim().equals(jwt)) {
                throw new UnauthorizedException("登录校验失败");
            }
            // 检查用户权限，根据不同的URL模式和用户角色判断是否放行
            if (url.contains("/admin") && !role.equals("SUPER_ADMIN")) {
                throw new ForbiddenException("权限不足");
            }
            if (url.contains("/teacher") && !role.equals("SUPER_ADMIN") && !role.equals("TEACHER")) {
                throw new ForbiddenException("权限不足");
            }
            if (url.contains("/student") && !role.equals("SUPER_ADMIN") && !role.equals("STUDENT")) {
              throw new ForbiddenException("权限不足");
            }

        // 令牌合法，放行
        log.info("令牌合法, 放行, 用户ID: {}, 来自 IP: {}", userId, remoteAddr);
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
