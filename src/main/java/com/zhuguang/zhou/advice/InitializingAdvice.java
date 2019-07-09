package com.zhuguang.zhou.advice;

import com.alibaba.fastjson.JSON;
import com.zhuguang.zhou.intercept.PropertyIntercept;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理视图解析器的先后顺序把自己的解析先于
 * RequestResponseBodyMethodProcessor
 */
@Configuration
public class InitializingAdvice implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        this.handlersSort(handlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(handlers);

    }

    private void handlersSort(List<HandlerMethodReturnValueHandler> handlers) {
        System.out.println("...........beaufort.........." + handlers);
        HandlerMethodReturnValueHandler response = null;
        int responseIndexOf = 0;
        int targetIndexOf = 0;
        for (HandlerMethodReturnValueHandler handler : handlers) {
             if (handler instanceof RequestResponseBodyMethodProcessor) {
                 responseIndexOf = handlers.indexOf(handler);
                 response = handler;
             }
             if (handler instanceof PropertyIntercept) {
                 targetIndexOf = handlers.indexOf(handler);
                 handlers.set(responseIndexOf,handler);
                 handlers.set(targetIndexOf,response);
             }
        }
        System.out.println("........... after.........." + handlers);
    }
}
