package com.wx.mini.robot;

import com.mumu.listenner.KQMSGAdapter;
import com.mumu.msg.*;
import com.wx.mini.client.RewriteKQWebClient;
import com.wx.mini.service.AiRobotService;
import com.wx.mini.service.apiprocessor.EarthlySweetApiService;
import com.wx.mini.service.apiprocessor.RainbowFartApiService;
import com.wx.mini.utils.ApplicationContextHodler;
import com.wx.mini.utils.QQConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author alan.chen
 * @date 2019/11/4 6:48 PM
 */
@Slf4j
public class MyQQAdapter extends KQMSGAdapter {

    private RewriteKQWebClient kqWebClient;

    private static final String MSG_SYMBOL_CHI = "？";
    private static final String MSG_SYMBOL_ENG = "?";

    public MyQQAdapter() {
    }

    public MyQQAdapter(RewriteKQWebClient kqWebClient) {
        this.kqWebClient = kqWebClient;
    }

    // 因为该对象是通过手动new的方式创建，所有不能通过spring自动注入bean, 需要通过applicationContent获取到spring的bean
    private static EarthlySweetApiService earthlySweetApiService = ApplicationContextHodler.getBean(EarthlySweetApiService.class).orElse(null);
    private static RainbowFartApiService rainbowFartApiService  = ApplicationContextHodler.getBean(RainbowFartApiService.class).orElse(null);
    private static MessageMng messageMng = ApplicationContextHodler.getBean(MessageMng.class).orElse(null);
    private static AiRobotService aiRobotService = ApplicationContextHodler.getBean(AiRobotService.class).orElse(null);


    /**
     * 接收私聊消息
     */
    @Override
    public void Re_MSG_Private(RE_MSG_Private msg) {
        System.out.println("接收到私聊信息 from:"+msg.getFromqq()+" \t msg:"+msg.getMsg());
        log.info("接收到私聊信息 from:"+msg.getFromqq()+" \t msg:"+msg.getMsg());
        String replyMsg = msg.getMsg();
        String fromqq = msg.getFromqq();
        String robotMsg = "";
        if(replyMsg.startsWith(MSG_SYMBOL_ENG) || replyMsg.startsWith(MSG_SYMBOL_CHI)) {
            // 如果是选择，则判断选项
            String replyNum =replyMsg.substring(1);
            QQConstant byValue = QQConstant.getByValue(replyNum);
            if(byValue != null) {
                switch (byValue) {
                    case EARTH_SWEET_WORD :
                        robotMsg = earthlySweetApiService.getEarthSweetWord();
                        messageMng.autoHandlerReplyMsg(fromqq, robotMsg);
                        break;
                    case RAINBOW_WORD :
                        robotMsg = rainbowFartApiService.getRainbowFartWord();
                        messageMng.autoHandlerReplyMsg(fromqq, robotMsg);
                        break;
                    case CHAT :
                        messageMng.autoHandlerReplyMsg(fromqq, "可以开始聊天啦，你想聊什么？");
                        break;
                    default:
                        messageMng.autoHandlerReplyMsg(fromqq, "输的什么 不认识");
                }
            } else {
                // 如果回复的序号是其他数字
                robotMsg = aiRobotService.getRobotAutoReplyMsg(replyMsg);
                messageMng.autoHandlerReplyMsg(fromqq, robotMsg);
            }
        } else {
            //如果是回复其他内容，则智能回复
            robotMsg = aiRobotService.getRobotAutoReplyMsg(replyMsg);
            messageMng.autoHandlerReplyMsg(fromqq, robotMsg);
        }
//        kqWebClient.sendPrivateMSG(msg.getFromqq(),"你好,接收到了你的消息：" + msg.getMsg());
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
        // TODO 是否有at，如果有则处理
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
