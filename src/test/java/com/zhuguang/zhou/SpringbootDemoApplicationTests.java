package com.zhuguang.zhou;

import com.zhuguang.zhou.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDemoApplicationTests {

    @Autowired
    @Qualifier("httpTemplate")
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        String url = "http://localhost:8085/getUserInfo?name=zhangshan";
        User user = restTemplate.getForObject(url, User.class);
        System.out.println("user：" + user);
    }


}
