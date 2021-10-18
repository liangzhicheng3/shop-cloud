package com.liangzhicheng.shop.config.websocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@ServerEndpoint("/{uuid}")
@Component
public class WebSocketServer {

    /**
     * 用于存放所有在线客户端
     */
    public static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过此来给客户端发送数据
     */
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid) {
        log.info("[Socket 服务] 用户{}已连接...", uuid);
        //添加session作为长连接会话
        clients.put(uuid, session);
    }

    @OnClose
    public void onClose(@PathParam("uuid") String uuid) {
        log.info("[Socket 服务] 用户{}已断开...", uuid);
        clients.remove(uuid);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("[Socket 服务] 连接异常：{}", error);
    }

}
