package io.github.qianlei.chapter2.proxyTest;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

public class GreetingThrowAdvice implements ThrowsAdvice {
    public void afterThrowing(Method method, Object[] args, Object target, Exception e) {
        System.out.println("---------throw Exception--------");
        System.out.println("Target Class: " + target.getClass().getName());
        System.out.println("Methond name: " + method.getName());
        System.out.println("Exception Message: " + e.getMessage());
        System.out.println("----------------------------------");
    }

}
