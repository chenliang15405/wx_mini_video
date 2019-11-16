package com.wx.mini.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wx.mini.service.RobotMessageCollService;
import com.wx.mini.service.apiprocessor.EarthlySweetApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息数据获取服务
 *
 * @author alan.chen
 * @date 2019/11/6 10:47 PM
 */
@Slf4j
@Transactional
@Service
public class RobotMessageCollServiceImpl implements RobotMessageCollService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EarthlySweetApiService earthlySweetApiService;

    @Value("${robot.weather.url}")
    private String weatherUrl;

    @Override
    public String getWeatherInfo() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(weatherUrl, String.class);
        if(responseEntity.getStatusCodeValue() == 200) {
            String body = responseEntity.getBody();
            Map<String,Object> map = JSON.parseObject(body, HashMap.class);
            System.out.println(map);
            Map<String, String> dataMap = JSON.parseObject(JSONObject.toJSONString(map.get("data")), HashMap.class);
            dataMap.get("address");
            dataMap.get("windPower");
            dataMap.get("windDirection");
            dataMap.get("weather");
            String format = String.format("%s: %s 今日%s 温度%s %s风",
                    dataMap.get("reportTime"), dataMap.get("address"), dataMap.get("weather"),
                    dataMap.get("temp"), dataMap.get("windPower"), dataMap.get("windDirection"));
           log.info("【天气信息】: {}", format);
            return format;
        }
        return null;
    }

    @Override
    public String getEarthSweetWord() {
        String content = earthlySweetApiService.getEarthSweetWord();
        log.info("随机土味情话：{}", content);
        return content;
    }

}
