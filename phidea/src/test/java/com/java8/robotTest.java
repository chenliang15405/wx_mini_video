package com.java8;

import com.alibaba.fastjson.JSON;
import com.wx.mini.ApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alan.chen
 * @date 2019/11/15 10:21 AM
 */
@RunWith(SpringRunner.class)
// 如果测试类和src下包名不一致，则需要指定启动类
@SpringBootTest(classes = ApiApplication.class)
public class robotTest {

    @Value("${robot.robotOption}")
    private Integer robotOption;
    @Value("${robot.qingyunke.api}")
    private String qingYunKeRobotApi;

    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void qingyunkeTest() {
        if(1 == robotOption) {
            String url = String.format(qingYunKeRobotApi, "这是一行代码");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            System.out.println(responseEntity);
            if(responseEntity != null && responseEntity.getStatusCodeValue() == 200) {
                System.out.println(responseEntity.getBody());
                Map<String,Object> map = JSON.parseObject(responseEntity.getBody(), HashMap.class);
                System.out.println(map.get("content"));
            }
        }
    }


}
