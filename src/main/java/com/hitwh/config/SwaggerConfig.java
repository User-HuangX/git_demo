package com.hitwh.config; // 替换为你的实际包名

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Swagger配置类
 * 用于定义OpenAPI的全局安全方案和元数据信息
 */
@SecurityScheme(
        name = "token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "token"),
        info = @Info(
                title = "ShiKe",
                description = "拾刻，是一个选课平台，基于这个平台教师可以设置课程，学生可以选择课程并对课程或老师做出评价，可以查询自己的课程",
                version = "1.0.0",
                contact = @Contact(
                        name = "黄湘湘",
                        email = "邮箱",
                        url="https://space.bilibili.com/381798268"
                )

        )
)
@SecurityRequirement(name = "all",scopes = "all scope")
public class SwaggerConfig {
    // 类中可以添加Swagger配置相关的方法或属性
}

