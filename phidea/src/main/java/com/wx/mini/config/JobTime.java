package com.wx.mini.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @auther alan.chen
 * @time 2019/10/10 3:31 PM
 */
@Getter
@Setter
@Configuration
@PropertySource(value = "classpath:application-quartz.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties(prefix = "quartz")
public class JobTime {

    private String articleCarwler;

    private String earthlySweetSentenceApi;

    private String rainbowFartApi;

    private String qqRobotMsgTimer;
}
