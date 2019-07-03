package com.zhuguang.zhou.annotation;



import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyFiltration {

    Class<?> clazz() default Class.class;

    String[] include() default {};

    String[] exclude() default {};
}
