package com.wx.mini.client;

import java.net.URI;

/**
 * @author alan.chen
 * @date 2019/11/4 6:46 PM
 */
public class KQClient {

    private RewriteKQWebClient kqWebClient;


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
        }catch (Exception e){
            System.err.println("init KQ client fail e:"+e.getMessage());
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        KQClient kqClient = new KQClient();
        kqClient.runClient("ws://127.0.0.1:25303");
    }

}
