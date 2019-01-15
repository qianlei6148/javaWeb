package io.github.qianlei.chapter2.test1;

public class HelloImpl implements Hello {
    @Override
    public void say(String name) {
        System.out.println("Hello!, " + name);
    }
}
