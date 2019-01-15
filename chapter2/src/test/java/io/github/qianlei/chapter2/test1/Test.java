package io.github.qianlei.chapter2.test1;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy(new HelloImpl());
        Hello hello1 = dynamicProxy.getProxy();
        hello1.say("jakk");

//        CGLibProxy cgLibProxy = new CGLibProxy();
//        Hello hello = cgLibProxy.getProxy(new GreetingImpl().getClass());
        Hello hello = CGLibProxy.getInstance().getProxy(new HelloImpl().getClass());
        hello.say("dddd");
    }
}
