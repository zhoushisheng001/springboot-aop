package com.zhuguang.zhou.proxy;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("=================不用实现了====================" + "args：" + JSON.toJSONString(args));
        Class<?> declaringClass = method.getDeclaringClass();
        Class<? extends Method> aClass = method.getClass();
        System.out.println("declaringClass：" + declaringClass + "，method：" +method.getName());
        return null;
    }
}
