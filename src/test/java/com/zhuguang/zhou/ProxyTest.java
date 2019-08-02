package com.zhuguang.zhou;

import com.zhuguang.zhou.proxy.UserInterface;
import com.zhuguang.zhou.proxy.UserProxy;
import org.junit.Test;

import java.lang.reflect.Proxy;

public class ProxyTest {

    /**
     * 测试动态代理没有实现只有接口
     */
    @Test
    public void test01 () {
        UserInterface userPro =(UserInterface) Proxy.newProxyInstance(UserInterface.class.getClassLoader(),
                new Class[]{UserInterface.class}, new UserProxy());
        userPro.say("江西");
        System.out.println("============================================");
    }
}
