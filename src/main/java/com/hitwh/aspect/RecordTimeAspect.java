package com.hitwh.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 记录方法执行时间的切面类
 * 使用AOP（面向切面编程）来拦截指定方法的执行，记录其耗时
 */
@Component
@Aspect
@Slf4j
public class RecordTimeAspect {

    /**
     * 环绕通知方法，用于记录目标方法的执行时间
     * 该方法会在目标方法调用之前和之后执行，以便计算执行时间
     *
     * @param joinPoint 切入点对象，包含被拦截方法的信息
     * @return 目标方法的执行结果
     * @throws Throwable 如果目标方法抛出异常，该方法也会抛出相同的异常
     */
    @Pointcut("@annotation(com.hitwh.annotation.RecordTime)")
    @Around("execution(* com.hitwh.service.impl.*.*(..)) && @annotation(com.hitwh.annotation.RecordTime)")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long beginTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 记录结束时间
        long endTime = System.currentTimeMillis();
        // 记录日志，输出方法名和执行时间
        log.info("方法{}耗时：{}ms", joinPoint.getSignature().getName(), endTime - beginTime);

        return result;
    }
}
