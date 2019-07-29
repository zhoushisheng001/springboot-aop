package com.zhuguang.zhou.intercept;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.zhuguang.zhou.annotation.JsonParam;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author zhoushisheng
 * 自定义参数解析器
 */
public class JsonPathArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    /**
     * //判断是否支持要转换的参数类型
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    /**
     *  当支持后进行相应的参数转换
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String body = getRequestBody(webRequest);
        Object val = null;

        try {
            val = JsonPath.read(body, parameter.getParameterAnnotation(JsonParam.class).value());
            if (parameter.getParameterAnnotation(JsonParam.class).required() && val == null) {
                throw new Exception(parameter.getParameterAnnotation(JsonParam.class).value() + "不能为空");
            }
        } catch (PathNotFoundException exception) {
            System.out.println(exception.getStackTrace());
            if (parameter.getParameterAnnotation(JsonParam.class).required()) {
//                throw new ParamCheckException(parameter.getParameterAnnotation(JsonParam.class).value() + "不能为空");
                throw exception;
            }
        }
        return val;

    }
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest.getAttribute(JSON_REQUEST_BODY);
        if (jsonBody == null) {
            try {
//                jsonBody = IOUtils.toString(servletRequest.getInputStream(),Charsets.toCharset("UTF-8"));
                jsonBody = IOUtils.toString(servletRequest.getInputStream(),"UTF-8");
                servletRequest.setAttribute(JSON_REQUEST_BODY, jsonBody);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;

    }

}
