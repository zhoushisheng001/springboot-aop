package com.zhuguang.zhou.service.impl;

import com.zhuguang.zhou.annotation.AopFilter;
import com.zhuguang.zhou.model.User;
import com.zhuguang.zhou.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @AopFilter
    @Override
    public User getAopInfo() {
        System.out.println("进入本来的方法。。。。。。");
        User user = new User();
        user.setId(1L);
        user.setUsername("张山");
        return user;
    }
}
