package com.zhuguang.zhou.controller;

import com.zhuguang.zhou.annotation.PropertyFiltration;
import com.zhuguang.zhou.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PropertyFiltration(clazz = User.class,exclude = {"password"})
    @GetMapping("/getUserInfo")
    public User getUserInfo () {
        logger.info("获得数据开始...");
        User user = new User(128L, "张山", "15818", "78715", new Date());
        return user;
    }
    //detaName","dataType","traceId","password
    @PropertyFiltration(clazz = ResponseData.class,exclude = {"dataType","traceId","data.password",
            "data.order.id","data.order.name","data.order.detaile.id","data.order.detaile.detaName"})
    @GetMapping("/getUserData")
    public ResponseData<User> getUserData () {
        ResponseData<User> resp = new ResponseData<>();
        User user = new User(128L, "张山", "15818", "78715", new Date());
        Order order = new Order();
        order.setId("12586");
        order.setCode("AXZ12586");
        order.setName("雪花旺旺饼干");
        user.setOrder(order);
        Detaile detaile = new Detaile();
        detaile.setDetaName("好吃的饼干");
        detaile.setId("781");
        detaile.setPrice(158);
        detaile.setGoods("食品");
        detaile.setNumber(15L);
        order.setDetaile(detaile);
        resp.setCode(12);
        resp.setMsg("sceess");
        resp.setTraceId("12586");
        resp.setData(user);
        return resp;
    }

    @GetMapping(path = "/getClazzInfo")
    @PropertyFiltration(clazz = ResponseData.class,exclude = {"dataType","data.studentList.love",
            "data.studentList.sex"})
    public ResponseData<Clazz> getClazzInfo () {
        ResponseData<Clazz> resp = new ResponseData<>();
        Clazz clazz = new Clazz();
        clazz.setId(158);
        clazz.setClazzName("五班");
        clazz.setStudentNum(65);
        Student stu1 = new Student();
        stu1.setAge(25);
        stu1.setName("张山");
        stu1.setLove("数学");
        stu1.setId(1);
        stu1.setSex("男");
        Student stu2 = new Student();
        stu2.setAge(26);
        stu2.setName("李思");
        stu2.setLove("语文");
        stu2.setId(2);
        stu2.setSex("男");
        clazz.setStudentList(new ArrayList<Student>(){{
            add(stu1);
            add(stu2);
        }});
        resp.setData(clazz);
        resp.setCode(12);
        resp.setMsg("sceess");
        resp.setTraceId("12588");
        return resp;

    }
}
