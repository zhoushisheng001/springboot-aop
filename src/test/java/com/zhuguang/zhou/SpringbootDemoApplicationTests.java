package com.zhuguang.zhou;

import com.zhuguang.zhou.model.Clazz;
import com.zhuguang.zhou.model.Student;
import com.zhuguang.zhou.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringbootDemoApplicationTests {


    private final static Logger logger = LoggerFactory.getLogger(SpringbootDemoApplicationTests.class);

    @Autowired
    @Qualifier("httpTemplate")
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        String url = "http://localhost:8085/getUserInfo?name=zhangshan";
        User user = restTemplate.getForObject(url, User.class);
        System.out.println("user：" + user);
    }


    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(20180506);
        list.add(20180508);
        list.add(20190704);
        System.out.println("list befort：" + list);
        sortList(list);
        System.out.println("list after：" + list);
        StringBuffer strbu = new StringBuffer();
        strbu.append(list.get(0)).append(",").append(list.get(1));
        logger.info("redis里面缓存的日期为dayList：" + String.valueOf(strbu));
    }


    //按照降序排序
    private static void sortList (List<Integer> dayCountList) {
        Collections.sort(dayCountList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {//按照时间降序
                return -(o1-o2);
            }
        });
    }


    @Test
    public void test02 () throws Exception {
        Class<Clazz> clazzClass = Clazz.class;
        Field field = clazzClass.getDeclaredField("studentList");
        System.out.println(field.getType());
        if (field.getType() == List.class) {
            System.out.println("list");
        }
        Type genericType = field.getGenericType();
        System.out.println(genericType);
        //说明是泛型类型
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt =  (ParameterizedType) genericType;
            Clazz type = (Clazz)pt.getActualTypeArguments()[0];
            System.out.println(type);
        }

    }

    @Test
    public void test03 () {
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        System.out.println(list);
        list.set(0,"ddd");
        System.out.println(list);
    }


}
