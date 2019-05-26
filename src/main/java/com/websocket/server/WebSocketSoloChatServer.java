package com.websocket.server;

import com.alibaba.fastjson.JSONObject;
import com.websocket.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import com.alibaba.fastjson.JSON;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocketSoloChatServer {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static CopyOnWriteArraySet<WebSocketSoloChatServer> webSocketSet = new CopyOnWriteArraySet<WebSocketSoloChatServer>();
    private static Map<String, Session> map = new HashMap<String, Session>();
    public Session session;
    public  String username;
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    private static Map<String, WebSocketSoloChatServer> clients = new ConcurrentHashMap<String, WebSocketSoloChatServer>();
    /**
     *  * 连接建立成功调用的方法
     *  
     */
    @OnOpen
    public void onOpen(HttpSession httpSession,Session session) {
        onlineNumber++;
        String username = (String) httpSession.getAttribute("username");
        logger.info("现在来连接的客户id：" + session.getId() + "用户名：" + username);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消
            //把自己的信息加入到map当中去
            clients.put(username, this);
            //给自己发一条消息：告诉自己现在都有谁在线
            Map<String, Object> map2 = new HashMap<>();
            map2.put("messageType", 3);
            //移除掉自己

            Set<String> set = clients.keySet();
            map2.put("onlineUsers", set);
            sendMessageTo(JSON.toJSONString(map2), username);
        } catch (IOException e) {
            logger.info(username + "上线的时候发生了错误");
        }
    }
        @OnError
        public void onError(Session session, Throwable error) {
            logger.info("服务端发生了错误"+error.getMessage());
            error.printStackTrace();
        }

    @OnClose
    public void onClose(HttpSession session)
    {
    String username = (String) session.getAttribute("username");
        onlineNumber--;
       //webSocketSet.remove(this);
       clients.remove(username);
        logger.info("连接关闭！");
    }



        /**
              * 收到客户端消息后调用的方法
              *
              * @param message 客户端发送过来的消息*/

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        try {
            logger.info("来自客户端消息：" + message+"客户端的id是："+session.getId());
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("message");
            String fromusername = jsonObject.getString("username");
            String tousername = jsonObject.getString("to");
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1 =new HashMap<>();
            map1.put("messageType",4);
            map1.put("textMessage",textMessage);
            map1.put("fromusername",fromusername);
            if(tousername.equals("All")){
                map1.put("tousername","所有人");
                sendMessageAll(JSON.toJSONString(map1),fromusername);
            }
            else{
                map1.put("tousername",tousername);
                //sendMessageTo(JSON.toJSONString(map1),tousername);
            }
            if(userService.dolike(username,tousername)){
                sendMessageTo(JSON.toJSONString(map1),tousername);
            }
            else{
                logger.info("未添加好友，无法发送消息");
            }
        }
        catch (Exception e){
            logger.info("发生了错误了");
        }

    }






    public void sendMessageTo (String message, String ToUserName) throws IOException {
        for (WebSocketSoloChatServer item : clients.values()) {
            if (item.username.equals(ToUserName)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    public void sendMessageAll (String message, String FromUserName) throws IOException {
        for (WebSocketSoloChatServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount () {
        return onlineNumber;
    }
}
