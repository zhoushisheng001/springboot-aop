package com.zhuguang.zhou.aop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhuguang.zhou.annotation.PropertyFiltration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

@Aspect
@Component
public class PropertyAspect {

    private static final String point ="@annotation(com.zhuguang.zhou.annotation.AopFilter)";

    @Pointcut(point)
    public void pointCut (){};

    @Before("pointCut()" )
    public void beforeInfo (JoinPoint joinPoint) {
        System.out.println(".............方法执行前切面执行.............." );
    }


    @AfterReturning(pointcut = "pointCut()" ,returning = "returnValue")
    public void afterInfo (JoinPoint joinPoint,Object returnValue) {
        try {
            System.out.println("===========执行前的数据===============" + returnValue);
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            PropertyFiltration annotation = method.getAnnotation(PropertyFiltration.class);
            String[] exclude = annotation.exclude();
            Class<?> clazz = returnValue.getClass();
            for (String name : exclude) {
                 Field field = clazz.getDeclaredField(name);
                 if (field != null) {
                     field.setAccessible(true);
                     field.set(returnValue,null);
                     JsonIgnore annotation1 = field.getAnnotation(JsonIgnore.class);
                     System.out.println("修改前的字段属性" + annotation1.value());
                     InvocationHandler h = Proxy.getInvocationHandler(annotation1);
                     Field hField = h.getClass().getDeclaredField("memberValues");
                     hField.setAccessible(true);
                     Map memberValues = (Map) hField.get(h);
                     // 修改 value 属性值
                     memberValues.put("value", false);
                     System.out.println("修改后的结构:" + annotation1.value());
                 }
            }
            System.out.println("..........执行后面的数据........." + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
