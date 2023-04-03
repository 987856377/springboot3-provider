package com.springboot.provider.module.common.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ServerEndpoint("/websocket")
public class WebSocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicLong ONLINE_COUNT = new AtomicLong(0L);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final Map<String, CopyOnWriteArraySet<WebSocketServer>> WEB_SOCKET_SERVER_MAP = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        String id = session.getId();
//        String userId = this.session.getQueryString();
        String userId = this.session.getUserPrincipal().getName();

        if (StringUtils.hasText(userId)) {
            if (WEB_SOCKET_SERVER_MAP.get(userId) == null) {
                CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
                webSocketSet.add(this);
                WEB_SOCKET_SERVER_MAP.put(userId, webSocketSet);
                addOnlineCount(); // 在线数加1
            } else {
                WEB_SOCKET_SERVER_MAP.get(userId).add(this);
            }
        }

        LOGGER.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        sendMessage("server reply: 连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
//        String userId = this.session.getQueryString();
        String userId = this.session.getUserPrincipal().getName();
        CopyOnWriteArraySet<WebSocketServer> webSocketServers = WEB_SOCKET_SERVER_MAP.get(userId);
        webSocketServers.remove(this);
        subOnlineCount(); // 在线数减1
        LOGGER.info("{}连接关闭！当前在线人数为{}", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("来自客户端的消息:" + message);
        sendMessage("server reply: " + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("发生错误", error);
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);    // 向此session发送消息
            LOGGER.info("消息发送成功：{}", message);
        } catch (IOException e) {
            LOGGER.error("消息发送失败", e);
        }
    }

    /**
     * 实现服务器主动推送, 单用户单端登录
     */
    public void sendUserSingleMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);    // 向此session发送消息
            LOGGER.info("消息发送成功：{}", message);
        } catch (IOException e) {
            LOGGER.error("消息发送失败", e);
        }
    }

    /**
     * 实现服务器主动推送, 单用户多端登录
     */
    public void sendUserMultiMessage(String message) {
        WEB_SOCKET_SERVER_MAP.get(this.session.getUserPrincipal().getName()).forEach(webSocketServer -> {
            try {
                webSocketServer.session.getBasicRemote().sendText(message);
                LOGGER.info("消息发送成功：{}", message);
            } catch (IOException e) {
                LOGGER.error("消息发送失败", e);
            }
        });
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(MessageDTO message) throws IOException {
        if (message.getReceiveUserId() != null && !"".equals(message.getReceiveUserId())) {
            // 全局发送消息
            if ("0".equals(message.getReceiveUserId())) {
                WEB_SOCKET_SERVER_MAP.forEach((key, webSocketServers) -> {
                    webSocketServers.forEach(webSocketServer -> {
                        webSocketServer.sendMessage(message.getContent());
                    });
                });
            } else if (WEB_SOCKET_SERVER_MAP.get(message.getReceiveUserId()) != null) {
                WEB_SOCKET_SERVER_MAP.get(message.getReceiveUserId()).forEach(webSocketServer -> {
                    webSocketServer.sendMessage(message.getContent());
                });
            } else {
                LOGGER.error("没有找到消息发送的目标对象");
            }
        } else {
            LOGGER.error("没有找到消息发送的目标对象");
        }
    }

    public static synchronized long getOnlineCount() {
        return ONLINE_COUNT.get();
    }

    public static synchronized long addOnlineCount() {
        return WebSocketServer.ONLINE_COUNT.incrementAndGet();
    }

    public static synchronized long subOnlineCount() {
        return WebSocketServer.ONLINE_COUNT.decrementAndGet();
    }
}




