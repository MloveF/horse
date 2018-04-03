package com.kaishengit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component  //支持注解添加bean管理
@Aspect //实现AOP切面工程
public class LogAspect {

    //标记一个切入点表达式
    @Pointcut("execution(* com.kaishengit.service.*.*(..))")
    public void pointcut(){}

    //前置通知
    @Before("pointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + " before Advice");
    }

    //后置通知
    @AfterReturning(value = "pointcut()",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + " afterReturning Advice ->" + result);
    }

  /*  //异常通知
    @AfterThrowing(value = "pointcut()",throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + " afterThrowing Advice ->" + ex.getMessage());
    }

    //最终通知
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + " after Advice");
    }

    //环绕通知
    @Around("pointcut()")
    public Object aroundAdivce(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }*/
}
