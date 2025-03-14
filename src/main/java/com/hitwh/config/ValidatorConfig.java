package com.hitwh.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于定义验证器和相关处理
 */
@Configuration
public class ValidatorConfig {

    /**
     * 配置快速失败模式，遇到一个不匹配直接返回，而不是扫描所有是否匹配：
     * 1. 普通模式（默认模式）：会校验完所有的属性，然后返回所有的验证失败信息
     * 2. 快速失败模式：只要有一个验证失败，则返回
     *
     * @return Validator 实例，用于验证对象是否符合规范
     */
    @Bean
    public static Validator validator() {
        return Validation
                .byProvider(HibernateValidator.class)
                .configure().failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }
}
