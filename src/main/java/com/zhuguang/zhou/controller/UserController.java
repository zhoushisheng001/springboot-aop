package com.zhuguang.zhou.controller;

import com.zhuguang.zhou.annotation.PropertyFiltration;
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

    @PropertyFiltration(clazz = ResponseData.class,exclude = ("user.password"))
    @GetMapping("/getUserData")
    public ResponseData<User> getUserData () {
        ResponseData<User> resp = new ResponseData<>();
        User user = new User(128L, "张山", "15818", "78715", new Date());
        resp.setCode(12);
        resp.setMsg("sceess");
        resp.setTraceId("12586");
        resp.setData(user);
        return resp;
    }
}
