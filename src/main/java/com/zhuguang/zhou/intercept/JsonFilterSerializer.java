package com.zhuguang.zhou.intercept;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.zhuguang.zhou.model.ResponseData;
import com.zhuguang.zhou.model.User;
import com.zhuguang.zhou.utills.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author zhoushisheng
 * 对象返回过滤解析器
 */
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
            Map<String, Set> listMap = buildExclude(chaldClazz, exclude);
            exclude = CollectionUtils.isNotEmpty (listMap.get(FILTER_EXCLUDE_NAME)) ?
                    (String[]) listMap.get(FILTER_EXCLUDE_NAME).toArray(new String[]{}):new String[]{};
            System.out.println("exclude：" + JSON.toJSONString(exclude));
            Class[] classes = CollectionUtils.isNotEmpty(listMap.get(FILTER_EXCLUDE_CLAZZ)) ?
                    (Class[]) listMap.get(FILTER_EXCLUDE_CLAZZ).toArray(new Class[]{}):new Class[]{};
            mapper.setFilterProvider(new SimpleFilterProvider()
                    .addFilter(DYNC_EXCLUDE, SimpleBeanPropertyFilter.serializeAllExcept(exclude)));
            System.out.println("excludeClazz：" + JSON.toJSONString(classes));
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
    private Map<String,Set> buildExclude (Class clazz, String[] exclude) {
        try {
            Map<String,Set> retMap = new HashMap<>();
            Set<String> filters = new HashSet<>();
            for (String exc : exclude) {
                 if (!exc.contains(".")) {
                     filters.add(exc);
                 } else if (exc.contains("data.")) {
                     exc = exc.replaceAll("data.","");
                     if (exc.contains(".")) {
                         Map<String,Set> amonMap = this.recBuildExclude(clazz, exc.split("\\."));
                         buildRetNameClazz(retMap, amonMap);
                     } else {
                         filters.add(exc);
                     }
                 }
            }
            if (retMap.size() > 0 ) {
                Set list = retMap.get(FILTER_EXCLUDE_NAME);
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
     * 构建返回的名称和类
     * @param retMap
     * @param amonMap
     */
    private void buildRetNameClazz(Map<String, Set> retMap, Map<String, Set> amonMap) {
        Set amonNameList = amonMap.get(FILTER_EXCLUDE_NAME);
        Set amonClazzList = amonMap.get(FILTER_EXCLUDE_CLAZZ);
        Set nameList = retMap.get(FILTER_EXCLUDE_NAME);
        Set clazzList = retMap.get(FILTER_EXCLUDE_CLAZZ);
        if (CollectionUtils.isEmpty(nameList) && CollectionUtils.isNotEmpty(amonNameList)) {
            nameList = amonNameList;
            retMap.put(FILTER_EXCLUDE_NAME,nameList);
        } else if (CollectionUtils.isNotEmpty(nameList) && CollectionUtils.isNotEmpty(amonNameList)) {
            nameList.addAll(amonNameList);
        }
        if (CollectionUtils.isEmpty(clazzList) && CollectionUtils.isNotEmpty(amonClazzList)) {
            clazzList = amonClazzList;
            retMap.put(FILTER_EXCLUDE_CLAZZ,clazzList);
        } else if (CollectionUtils.isNotEmpty(clazzList) && CollectionUtils.isNotEmpty(amonClazzList)) {
            clazzList.addAll(amonClazzList);
        }
    }

    /**
     * data.order.id
     * 递归获得数据
     * @param clazz
     * @param exc
     * @return
     */
    private Map<String,Set> recBuildExclude (Class clazz, String[] exc) {
        try {
            Map<String,Set> retMap = new HashMap<>();
            Map<String, Set> listMap = new HashMap<>();
            Set<String> filters = new HashSet<>();
            Set<Class> classList = new HashSet<>();
            if (exc.length == 1) {
                filters.add(exc[0]);
            } else if (exc.length == 2) {
                Class<?> type = clazz.getDeclaredField(exc[0]).getType();
                Class<?> newType = buildClazz(clazz, exc[0], type);
                filters.add(exc[1]);
                classList.add(newType);
            }else if (exc.length > 2) {
                Class<?> type = clazz.getDeclaredField(exc[0]).getType();
                Class<?> newType = buildClazz(clazz, exc[0], type);
                classList.add(newType);
                String[] descStr = new String[exc.length-1];
                System.arraycopy(exc,1,descStr,0,exc.length-1);
                listMap = recBuildExclude(newType, descStr);
            }
            if (listMap.size() > 0) {
                filters.addAll(listMap.get(FILTER_EXCLUDE_NAME));
                classList.addAll(listMap.get(FILTER_EXCLUDE_CLAZZ));
            }
            retMap.put(FILTER_EXCLUDE_NAME, filters);
            retMap.put(FILTER_EXCLUDE_CLAZZ, classList);
            return retMap;
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }

    /**
     * 当数据类型为 list set Map 的时候找到对应的泛型数据类型
     * @param clazz
     * @param exc
     * @param type
     * @return
     * @throws NoSuchFieldException
     */
    private Class<?> buildClazz (Class clazz, String exc, Class<?> type) throws NoSuchFieldException {
        Class<?> newType = null;
        if (type == List.class) {
            Type genericType = clazz.getDeclaredField(exc).getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericType;
                Class inClazz = (Class) paramType.getActualTypeArguments()[0];
                newType = inClazz;
            }
        } else if (type == Map.class) {

        } else if (type == Set.class) {
            Type genericType = clazz.getDeclaredField(exc).getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericType;
                Class inClazz = (Class) paramType.getActualTypeArguments()[0];
                newType = inClazz;
            }
        } else {
            newType = type;
        }
        return newType;
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
