package com.zhuguang.zhou;

import com.zhuguang.zhou.cglib.UserServiceCglib;
import com.zhuguang.zhou.model.User;
import com.zhuguang.zhou.service.impl.UserServiceImpl;
import org.junit.Test;

public class TestCglib {

    @Test
    public void test01 () {
        UserServiceCglib cglib = new UserServiceCglib();
        UserServiceImpl bookFacedImpl = (UserServiceImpl) cglib.getInstance(new UserServiceImpl());
        User aopInfo = bookFacedImpl.getAopInfo();
        System.out.println("aopInfo：" + aopInfo);
    }
}
