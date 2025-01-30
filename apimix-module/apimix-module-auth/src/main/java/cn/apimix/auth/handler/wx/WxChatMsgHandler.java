package cn.apimix.auth.handler.wx;


import java.util.Map;

/**
 * @Author: Hor
 * @Date: 2024/10/13 下午12:04
 * @Version: 1.0
 */

public interface WxChatMsgHandler {

    WxChatMsgTypeEnum getMsgType();

    String dealMsg(Map<String, String> messageMap);

}
