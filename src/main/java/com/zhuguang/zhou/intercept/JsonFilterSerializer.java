package com.zhuguang.zhou.intercept;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.zhuguang.zhou.model.ResponseData;
import com.zhuguang.zhou.model.User;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonFilterSerializer {

    private static final String DYNC_INCLUDE = "DYNC_INCLUDE";//包含的标识
    private static final String DYNC_EXCLUDE = "DYNC_EXCLUDE";//过滤的标识
    private ObjectMapper mapper = new ObjectMapper();

    @JsonFilter(DYNC_EXCLUDE)
    interface DynamicExclude{

    }
    @JsonFilter(DYNC_INCLUDE)
    interface DynamicInclude{

    }

    /**
     * 过滤字段解析
     * @param clazz
     * @param retValue
     * @param include
     * @param exclude
     */
    public void filter (Class<?> clazz,Object retValue, String[] include , String[] exclude) {
        if (clazz == null) return;
        if (include != null && include.length > 0) {//包含的操作
            mapper.setFilterProvider(new SimpleFilterProvider()
                    .addFilter(DYNC_INCLUDE, SimpleBeanPropertyFilter.filterOutAllExcept(include)));
            mapper.addMixIn(clazz, DynamicInclude.class);
        } else if (exclude != null && exclude.length > 0) {
            mapper.setFilterProvider(new SimpleFilterProvider()
                    .addFilter(DYNC_EXCLUDE, SimpleBeanPropertyFilter.serializeAllExcept(exclude)));
            mapper.addMixIn(clazz, DynamicExclude.class);
            mapper.addMixIn(User.class,DynamicExclude.class);
        }
    }
    public String toJson(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }



    private Map<String,List<Object>> buildExclude (ResponseData retValue, String[] exclude) {
        Class dataType = retValue.getDataType();

        for (String exc : exclude) {
             if (!exc.contains(".")) {
                 break;
             } else {
                 String[] split = exc.split("\\.");

             }
        }

        return null;
    }






    public static void main(String[] args) throws Exception {
        JsonFilterSerializer jsonFilterSerializer = new JsonFilterSerializer();
        jsonFilterSerializer.testFilterJson();
    }
    public void testFilterJson() throws Exception{
        JsonFilterSerializer jsonFilter = new JsonFilterSerializer();
        ResponseData<User> resp = new ResponseData<>();
        User user = new User(128L, "张山", "15818", "78715", new Date());
        resp.setCode(12);
        resp.setMsg("sceess");
        resp.setTraceId("12586");
        resp.setData(user);
        jsonFilter.filter(ResponseData.class,resp, null, new String[]{"id","dataType","traceId","username"});
        Class realType = resp.getDataType();
        Class<User> userClass = User.class;
        System.out.println(jsonFilter.toJson(resp));
    }

}
