package com.zhuguang.zhou.intercept;

import com.zhuguang.zhou.annotation.PropertyFiltration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoushisheng
 */
public class PropertyIntercept implements HandlerMethodReturnValueHandler {


    private final static Logger logger = LoggerFactory.getLogger(PropertyIntercept.class);

    @Override
    public void handleReturnValue (Object returnObject, MethodParameter paramter,
                                  ModelAndViewContainer container, NativeWebRequest request) throws Exception {
        container.setRequestHandled(true);
        JsonFilterSerializer serializer = new JsonFilterSerializer();
        if(paramter.hasMethodAnnotation(PropertyFiltration.class)) {//如果有PropertyFiltration注解，则过滤返回的对象returnObject
            PropertyFiltration jsonFilter = paramter.getMethodAnnotation(PropertyFiltration.class);
            serializer.filter(jsonFilter.clazz() == null ?returnObject.getClass() : jsonFilter.clazz(),returnObject,jsonFilter.include(), jsonFilter.exclude());//调用过滤方法
        }
        HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(serializer.toJson(returnObject));
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(PropertyFiltration.class);
    }

}
