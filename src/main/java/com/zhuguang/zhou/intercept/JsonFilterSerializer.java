package com.zhuguang.zhou.intercept;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.zhuguang.zhou.model.ResponseData;
import com.zhuguang.zhou.model.User;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class JsonFilterSerializer {

    private static final String DYNC_INCLUDE = "DYNC_INCLUDE";//包含的标识
    private static final String DYNC_EXCLUDE = "DYNC_EXCLUDE";//过滤的标识

    private static final String FILTER_EXCLUDE_NAME = "FILTER_EXCLUDE_NAME";//剔除的字段
    private static final String FILTER_EXCLUDE_CLAZZ = "FILTER_EXCLUDE_CLAZZ";//剔除的类
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
        if (!(retValue instanceof ResponseData)) throw new RuntimeException("返回数据类型异常必须为ResponseData...");
        Class chaldClazz = ((ResponseData) retValue).getDataType();
        if (include != null && include.length > 0) {//包含的操作
            mapper.setFilterProvider(new SimpleFilterProvider()
                    .addFilter(DYNC_INCLUDE, SimpleBeanPropertyFilter.filterOutAllExcept(include)));
            mapper.addMixIn(clazz, DynamicInclude.class);
        } else if (exclude != null && exclude.length > 0) {
            Map<String, List> listMap = buildExclude(chaldClazz, exclude);
            exclude = (String[]) listMap.get(FILTER_EXCLUDE_NAME).toArray(new String[]{});
            Class[] classes = (Class[]) listMap.get(FILTER_EXCLUDE_CLAZZ).toArray(new Class[]{});
            mapper.setFilterProvider(new SimpleFilterProvider()
                    .addFilter(DYNC_EXCLUDE, SimpleBeanPropertyFilter.serializeAllExcept(exclude)));
            mapper.addMixIn(clazz, DynamicExclude.class);
            mapper.addMixIn(chaldClazz,DynamicExclude.class);
            this.addMixInExclude(mapper,classes);
        }
    }

    /**
     * 构建
     * @param mapper
     * @param classes
     */
    private void addMixInExclude(ObjectMapper mapper, Class[] classes) {
        for (Class chiClazz : classes) {
            mapper.addMixIn(chiClazz,DynamicExclude.class);
        }
    }

    public String toJson(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }


    /**
     * 组合需要剔除的数据类型
     * @param clazz
     * @param exclude
     * @return
     * data.id
     * data.order.id
     */
    private Map<String,List> buildExclude (Class clazz, String[] exclude) {
        try {
            Map<String,List> retMap = new HashMap<>();
            List<String> filters = new ArrayList<>();
            for (String exc : exclude) {
                 if (!exc.contains(".")) {
                     filters.add(exc);
                 } else if (exc.contains("data.")) {
                     exc = exc.replaceAll("data.","");
                     if (exc.contains(".")) {
                         retMap = this.recBuildExclude(clazz, exc.split("\\."));
                     } else {
                         filters.add(exc);
                     }
                 }
            }
            if (retMap.size() > 0 ) {
                List list = retMap.get(FILTER_EXCLUDE_NAME);
                list.addAll(filters);
            } else {
                retMap.put(FILTER_EXCLUDE_NAME, filters);
            }
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * data.order.id
     * 递归获得数据
     * @param clazz
     * @param exc
     * @return
     */
    private Map<String,List> recBuildExclude (Class clazz, String[] exc) {
        try {
            Map<String,List> retMap = new HashMap<>();
            List<String> filters = new ArrayList<>();
            List<Class> classList = new ArrayList<>();
            if (exc.length == 1) {
                filters.add(exc[0]);
            } else if (exc.length == 2) {
                Class<?> type = clazz.getDeclaredField(exc[0]).getType();
                filters.add(exc[1]);
                classList.add(type);
            }else if (exc.length > 2) {
                Class<?> type = clazz.getDeclaredField(exc[0]).getType();
                classList.add(type);
                String[] descStr = new String[exc.length-1];
                System.arraycopy(exc,1,descStr,0,exc.length-1);
                recBuildExclude(type,descStr);
            }
            retMap.put(FILTER_EXCLUDE_NAME, filters);
            retMap.put(FILTER_EXCLUDE_CLAZZ, classList);
            return retMap;
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
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
