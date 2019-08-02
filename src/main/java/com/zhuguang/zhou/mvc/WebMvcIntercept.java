package com.zhuguang.zhou.mvc;

import com.zhuguang.zhou.intercept.JsonPathArgumentResolver;
import com.zhuguang.zhou.intercept.PropertyIntercept;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author zhoushisheng
 *  添加返回值处理
 */
@Configuration
public class WebMvcIntercept implements WebMvcConfigurer {

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new PropertyIntercept());

    }

    /** 在方法参数上面加 @JsonParam
     * 添加参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JsonPathArgumentResolver());
    }


    /**
     * 处理跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE","PATCH")
                .allowedHeaders("*")
                .maxAge(3600)  //单位为秒1h失效（跨域是前端发送ajax请求限制的）
                .allowCredentials(true);
    }

/**
     * 其它资源路径
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("/");//所有
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.jsp("/WEB-INF/jsp/", ".jsp");
//    }

}
