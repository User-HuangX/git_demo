package com.hitwh.aspect;

import com.hitwh.exception.InvalidRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * AOP切面类，用于参数验证
 * 利用AspectJ的切面编程，拦截指定方法执行前的调用，进行参数验证
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ParameterValidationAspect {

    /**
     * 验证器，用于验证方法参数是否符合规范
     */
    private final Validator validator;

    /**
     * 环绕通知方法，用于在方法调用前后执行自定义逻辑
     * 本方法主要用于拦截服务层方法调用，验证其参数是否符合规范
     *
     * @param joinPoint 切入点对象，包含被拦截方法的信息
     * @param param 被验证的参数
     * @return 被拦截方法的执行结果
     * @throws Throwable 如果参数验证失败或方法执行出错
     */
    @Around("execution(* com.hitwh..*(..)) && args(param)")
    public Object validateParameters(ProceedingJoinPoint joinPoint, Object param) throws Throwable {
        // 验证参数是否符合规范
        Set<ConstraintViolation<Object>> violations = validator.validate(param);
        // 如果验证失败，抛出异常并给出失败原因
        if (!violations.isEmpty()) {
            throw new InvalidRequestException("参数验证失败： " + violations.iterator().next().getMessage());
        }
        // 如果验证成功，继续执行被拦截的方法

        return joinPoint.proceed();
    }
}
