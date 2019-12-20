package com.wx.mini;

import com.wx.mini.client.KQClient;
import com.wx.mini.robot.QQRobot;
import com.wx.mini.service.RobotMessageCollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author alan.chen
 * @date 2019/11/5 7:01 PM
 */
@Slf4j
@Component
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final KQClient kqClient = new KQClient();
    @Autowired
    private QQRobot qqRobot;

    @Value("${kqHost}")
    private String kqHost;
    @Autowired
    private RobotMessageCollService robotMessageCollService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("*************KQ CLIENT STSRT CONNECTION*****************" );
        try {
            // 链接kqclient
//            qqRobot.runClient(kqHost);
//            robotMessageCollService.getEarthSweetWord();
        } catch (Exception e) {
            log.error("启动Robot异常：{}", e);
        }
        log.info("*************KQ CLIENT END*****************" );
    }

}
