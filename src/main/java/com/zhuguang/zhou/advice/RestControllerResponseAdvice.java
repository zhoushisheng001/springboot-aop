package com.zhuguang.zhou.advice;

import com.alibaba.fastjson.JSON;
import com.zhuguang.zhou.annotation.PropertyFiltration;
import com.zhuguang.zhou.intercept.JsonFilterSerializer;
import com.zhuguang.zhou.model.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 对返回的结果进行处理在找到返回结果处理器以前
 * 返回结果处理器统一接口
 */
//@RestControllerAdvice
//@ControllerAdvice
public class RestControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite (Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 对body进行封装处理
        if (body instanceof String) {
            String msg = (String) body;
            System.out.println("msg:" + msg);
           return msg;
        } else if (body instanceof ResponseData) {
            ResponseData resp = (ResponseData) body;
            JsonFilterSerializer serializer = new JsonFilterSerializer();
            if(returnType.hasMethodAnnotation(PropertyFiltration.class)) {//如果有PropertyFiltration注解，则过滤返回的对象returnObject
                PropertyFiltration jsonFilter = returnType.getMethodAnnotation(PropertyFiltration.class);
                serializer.filter(jsonFilter.clazz() == null ?resp.getClass() : jsonFilter.clazz(),resp,jsonFilter.include(), jsonFilter.exclude());//调用过滤方法
            }
            System.out.println("data:" + JSON.toJSONString(resp));
            return resp;
        }

        return null;
    }
}
