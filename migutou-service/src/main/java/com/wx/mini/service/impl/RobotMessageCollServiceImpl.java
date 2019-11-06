package com.wx.mini.service.impl;

import com.wx.mini.service.RobotMessageCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 消息数据获取服务
 *
 * @author alan.chen
 * @date 2019/11/6 10:47 PM
 */
@Transactional
@Service
public class RobotMessageCollServiceImpl implements RobotMessageCollService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${robot.weather.url}")
    private String weatherUrl;

    @Override
    public String getWeatherInfo() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(weatherUrl, String.class);
        System.out.println(responseEntity);

        return null;
    }

    @Override
    public String getEarthSweetWord() {
        return null;
    }

}
