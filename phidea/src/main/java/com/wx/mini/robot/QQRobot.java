package com.wx.mini.robot;

import com.wx.mini.client.MyQQAdapter;
import com.wx.mini.client.RewriteKQWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * QQ机器人启动类
 *
 * @author alan.chen
 * @date 2019/11/5 5:27 PM
 */
@Component
public class QQRobot {

    private RewriteKQWebClient kqWebClient;

    @Autowired
    private MessageMng messageMng;


    public void runClient(String host){
        try {
            if(kqWebClient == null){
                //连接coolq服务器
                kqWebClient = new RewriteKQWebClient(new URI(host));
            }
            //消息监听适配器
            MyQQAdapter myQQAdapter = new MyQQAdapter(kqWebClient);
            //监听消息
            kqWebClient.addQQMSGListenner(myQQAdapter);

            // 启动之后第一次发送消息设置
            messageMng.appStartHandler(kqWebClient);
        }catch (Exception e){
            System.err.println("init KQ client fail e:"+e.getMessage());
            e.printStackTrace();
        }
    }


}
