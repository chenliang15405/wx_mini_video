package com.wx.mini.robot;

import com.wx.mini.client.RewriteKQWebClient;
import com.wx.mini.service.RobotMessageCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 机器人发送消息类
 *
 * @author alan.chen
 * @date 2019/11/6 10:34 PM
 */
@Component
public class MessageMng {

    private RewriteKQWebClient kqWebClient;

    @Autowired
    private RobotMessageCollService messageService;

    /**
     * 初始化发送消息处理
     * @param kqWebClient
     */
    public void appStartHandler(RewriteKQWebClient kqWebClient) {
        this.kqWebClient = kqWebClient;
        sendSweetWordForYou();
        sendServiceMsg();
    }


    /**
     * 发送甜蜜话语
     */
    private void sendSweetWordForYou() {
        String weatherInfo = messageService.getWeatherInfo();
        String earthSweetStr = messageService.getEarthSweetWord();
    }

    /**
     * 服务消息
     */
    private void sendServiceMsg() {
        kqWebClient.sendPrivateMSG("1165243776","你好，robot上线：\n请选择服务：\n 1.土味情话 \n 2. 彩虹屁 \n 3. 陪聊 \n 回复方式: ? + 序号");
    }



}
