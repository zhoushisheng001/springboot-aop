package com.zhuguang.zhou.controller;

import com.zhuguang.zhou.annotation.PropertyFiltration;
import com.zhuguang.zhou.model.Detaile;
import com.zhuguang.zhou.model.Order;
import com.zhuguang.zhou.model.ResponseData;
import com.zhuguang.zhou.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PropertyFiltration(clazz = ResponseData.class,exclude = {"dataType","traceId","data.password",
            "data.order.id","data.order.name","data.order.detaile.id"})
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
}
