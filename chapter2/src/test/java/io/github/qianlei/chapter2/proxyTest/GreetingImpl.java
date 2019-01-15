package io.github.qianlei.chapter2.proxyTest;

public class GreetingImpl implements Greeting {

    @Override
    public void sayHello(String name) {
        System.out.println("Hello!, " + name);
        throw new RuntimeException("error");
    }

}
