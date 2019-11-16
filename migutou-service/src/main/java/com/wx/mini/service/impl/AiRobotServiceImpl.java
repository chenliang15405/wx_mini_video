package com.wx.mini.service.impl;

import com.alibaba.fastjson.JSON;
import com.wx.mini.service.AiRobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alan.chen
 * @date 2019/11/13 7:48 PM
 */
@Slf4j
@Service
public class AiRobotServiceImpl implements AiRobotService {

    @Value("${robot.robotOption}")
    private Integer robotOption;
    @Value("${robot.qingyunke.api}")
    private String qingYunKeRobotApi;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public String getRobotAutoReplyMsg(String replyMsg) {
        // 根据不同的option使用不同的api
        if(robotOption == 1) {
            String url = String.format(qingYunKeRobotApi, replyMsg);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if(responseEntity != null && responseEntity.getStatusCodeValue() == 200) {
                log.info("智能回复：{}", responseEntity.getBody());
                Map<String,Object> map = JSON.parseObject(responseEntity.getBody(), HashMap.class);
                return String.valueOf(map.get("content"));
            }
        }
        return null;
    }

}
