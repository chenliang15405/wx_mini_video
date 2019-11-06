package com.wx.mini.client;

import com.mumu.listenner.KQMSGAdapter;
import com.mumu.msg.*;

/**
 * @author alan.chen
 * @date 2019/11/4 6:48 PM
 */
public class MyQQAdapter extends KQMSGAdapter {

    private RewriteKQWebClient kqWebClient;


    public MyQQAdapter(RewriteKQWebClient kqWebClient) {
        this.kqWebClient = kqWebClient;
    }

    /**
     * 接收私聊消息
     */
    @Override
    public void Re_MSG_Private(RE_MSG_Private msg) {
        System.out.println("接收到私聊信息 from:"+msg.getFromqq()+" \t msg:"+msg.getMsg());
        kqWebClient.sendPrivateMSG(msg.getFromqq(),"你好,接收到了你的消息：" + msg.getMsg());
    }

    @Override
    public void RE_MSG_FORUM(RE_MSG_Forum msg) {
        System.out.println("接收到消息 groupName:"+msg.getFromQQ() + "qq:"+msg.getFromQQ() + "msg:"+msg.getMsg());
    }

    /**
     * 接收群消息
     */
    @Override
    public void RE_MSG_Group(RE_MSG_Group msg) {
        System.out.println("接收到群聊消息 groupName:"+msg.getFromGroupName() + "\t qq:"+msg.getFromQQ() + "\t msg:"+msg.getMsg() + "\t act" + msg.getAct());
    }

    @Override
    public void RE_EXAMPLE_ADDFRIEND(AddFriendExample msg) {
        System.out.println("好友添加成功, action : " + msg.getFromQQ() + "    " + msg.getSubType());
    }

    @Override
    public void RE_ASK_ADDFRIEND(AddFriends msg) {
        System.out.println("请求添加好友,qq : " + msg.getFromQQ());
    }

}
