package com.zhuguang.zhou;

import com.zhuguang.zhou.clazz.AopSUpperImpl;
import com.zhuguang.zhou.clazz.AopSupper;
import org.junit.Test;

import java.lang.reflect.Method;

public class TestAop {

    @Test
    public void test01 () {
        AopSupper aopSupper = new AopSUpperImpl();
        Class<? extends AopSupper> aClass = aopSupper.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        Class<?> superclass = aClass.getSuperclass();
        System.out.println("superclass：" + superclass);
        Class<?> superclass1 = superclass.getSuperclass();
        System.out.println("superclass1：" + superclass1);
        Class<?> superclass2 = superclass1.getSuperclass();
        System.out.println("superclass2：" + superclass2);
        Class<?>[] classes = aClass.getClasses();
        System.out.println("classes：" + classes);
        for (Method method : declaredMethods) {
             System.out.println("method：" + method);
        }
    }
}
