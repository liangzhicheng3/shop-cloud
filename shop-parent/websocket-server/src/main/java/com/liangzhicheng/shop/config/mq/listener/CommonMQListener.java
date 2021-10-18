package com.liangzhicheng.shop.config.mq.listener;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.utils.JSONUtil;
import com.liangzhicheng.shop.config.websocket.WebSocketServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

@Slf4j
@Getter
public abstract class CommonMQListener {

    private String logTag = "[客户端消息通知]";

    protected void sendMessage(String uuid, ResponseResult<?> responseResult){
        try{
            //最大重试次数
            int maxNum = 3;
            Session session = null;
            do{
                //根据uuid获取对应的客户端
                session = WebSocketServer.clients.get(uuid);
                if(session == null){
                    //当前获取为空睡500ms
                    Thread.sleep(500L);
                }
                maxNum--;
            }while(session == null && maxNum >= 0);
            if(session != null){
                //发送给客户端
                session.getBasicRemote().sendText(JSONUtil.toJSONString(responseResult));
            }else{
                log.error("{} 通知客户端消息失败，无法获取到用户{}的连接", logTag, uuid);
            }
        }catch(Exception e){
            log.error(logTag + " 通知客户端消息失败：{}", e);
        }
    }

}
