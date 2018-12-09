package com.mj;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
 
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.Response;
 



//@ServerEndpoint("/websocket/{user}")
@ServerEndpoint(value = "/mj")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    private static Hashtable sessions = new Hashtable();
 
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        addOnlineCount();           //在线数加1
        Response response = new Response(1, "连接成功");
        ObjectMapper mapper = new ObjectMapper();
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
        	 String message = mapper.writeValueAsString(response); //返回字符串
        	 sendMessage(message);
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }
	//	//连接打开时执行
	//	@OnOpen
	//	public void onOpen(@PathParam("user") String user, Session session) {
	//		currentUser = user;
	//		System.out.println("Connected ... " + session.getId());
	//	}
 
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
 
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
    	log.info("来自客户端的消息:" + message);
    	ObjectMapper mapper = new ObjectMapper(); 
    	mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);   
    	mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    	try {  
    	    //转换为HashMap对象  
    	    //HashMap jsonMap = mapper.readValue(message, HashMap.class);  
    	    Map<String, Map<String, Object>> maps = mapper.readValue(message, Map.class);  
    	    int code = Integer.parseInt(maps.get("code").toString());
    	    switch(code) {
    	       //存储uuid
    	       case(1):
    	    	   String uuid = maps.get("code").toString();
                   sessions.put(uuid, session);
    	       case(2):
    	    	   
    	    }
    	} catch (Exception e) {  
    	    e.printStackTrace();  
    	}  
    }
 
	/**
	 * 
	 * @param session
	 * @param error
	 */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
 
 
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
 
 
    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
    	//log.info(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

