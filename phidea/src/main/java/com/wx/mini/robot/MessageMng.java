package com.wx.mini.robot;

import com.wx.mini.client.RewriteKQWebClient;
import com.wx.mini.service.RobotMessageCollService;
import org.apache.commons.lang3.StringUtils;
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
        sendServiceMsg();
    }

    public void timerTaskMsg() {
        sendSweetWordForYou();
    }


    /**
     * 发送甜蜜话语
     */
    private void sendSweetWordForYou() {
        StringBuilder msg = new StringBuilder();
        String weatherInfo = messageService.getWeatherInfo();
        String earthSweetStr = messageService.getEarthSweetWord();
        if(StringUtils.isNotBlank(weatherInfo)) {
            msg.append(weatherInfo);
            msg.append("\n");
            msg.append("\n");
        }
        if(StringUtils.isNotBlank(earthSweetStr)) {
            msg.append("Sweet：" + earthSweetStr);
        }
        kqWebClient.sendPrivateMSG("846799633", msg.toString());
    }

    /**
     * 服务消息
     */
    private void sendServiceMsg() {
//        kqWebClient.sendPrivateMSG("1165243776","你好，robot上线：\n请选择服务：\n 1.土味情话 \n 2. 彩虹屁 \n 3. 陪聊 \n \n回复方式: ? + 序号，例如: ?1");
        kqWebClient.sendPrivateMSG("846799633","你好，robot上线：\n请选择服务：\n 1.土味情话 \n 2. 彩虹屁 \n 3. 陪聊 \n \n回复方式: ? + 序号，例如: ?1");
    }


    /**
     * 自动回复列表选择消息
     *
     * @param fromqq
     * @param robotMsg
     */
    public void autoHandlerReplyMsg(String fromqq, String robotMsg) {
        kqWebClient.sendPrivateMSG(fromqq,robotMsg);
    }


}
