package com.mj;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;


import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.dao.RoomDao;
import com.dao.UserDao;
import com.entity.Room;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.Response;
 
//@ServerEndpoint("/websocket/{user}")
@ServerEndpoint(value = "/mj")
@Component
public class WebSocketServer {
	
    private RedisTemplate<String, Serializable> redisTemplate;
	
    private JdbcTemplate jdbcTemplate;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    private static Hashtable<String, Object> sessions = new Hashtable<>();
    
    
 
    /**
     * 连接建立成功调用的方法*/
    @SuppressWarnings("unchecked")
	@OnOpen
    public void onOpen(Session session, EndpointConfig config) {
    	 log.info("user:{}", config.getUserProperties().get("user"));
        this.session = session;
        addOnlineCount();           //在线数加1
        Response response = new Response(1, "连接成功");
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("database.xml");
		jdbcTemplate = (JdbcTemplate) ctx.getBean("JdbcTemplateOne");
		redisTemplate = (RedisTemplate<String, Serializable>) ctx.getBean("redisTemplate");
		//ctx.close();
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
        	 sendMessage(response);
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
    	//统一返回消息
    	Response response = new Response(0, "操作失败");
    	try {  
    	    //转换为HashMap对象  
    	    //HashMap jsonMap = mapper.readValue(message, HashMap.class);  
    	    Map<String, Object> maps = mapper.readValue(message, Map.class); 
    	    String thisCode = maps.get("code").toString();
    	    //用户ID
    	    String uid   = maps.get("uid").toString();
    	    //获取data中的数据
    	    Map data = (Map)maps.get("data");
    	    //当前用户初始化
    	    //用户信息读取
     	    UserDao userdao = new UserDao(jdbcTemplate);
    	    Map user = userdao.inquire(uid);
    	    //设置返回的方法，用以前端判断
    	    response.setFid(thisCode);
    	    log.info("得到的uuid:"+thisCode);
    	    int code = Integer.parseInt(thisCode);
    	    switch(code) {
    	       //存储uuid
    	       case(1):
    	    	   String uuid = maps.get("data").toString();
                   sessions.put(uuid, session);
                   response.setCode(1);
                   response.setMsg("uuid同步成功");
	               sendMessage(response);
	               break;
	           //创建房间
    	       case(2):
    	    	   //先在数据库中创建房间
    	    	   RoomDao dao = new RoomDao(jdbcTemplate);
    	           //获取单场的基础价格
    	           String amount = data.get("amount").toString();
    	           String people = data.get("people").toString();
    	           //通过金额和人数来创建房间
    	           int roomId = dao.insert(amount,people);
    	           if(roomId > 0) {
    	        	   //进行房间初始化,将当前用户加入房间
    	        	   Room room = new Room(roomId,  Integer.parseInt(people));
    	        	   //添加东家
    	        	   String userId = user.get("user_id").toString();
    	        	   String name  = user.get("nick").toString();
    	        	   String avatar  = user.get("avatar").toString();
    	        	   int enterRoom = room.addClient(userId, name, avatar, 1);
    	        	   if(enterRoom == 0) {
    	        		   response.setMsg("人数超过限制，加入失败");
    	        		   sendMessage(response);
    	        		   break;
    	        	   } 
    	        	   System.out.println(room.player[0].nickName);
    	        	   //将房间信息存入redis,并跳转
    	               String roomJson = mapper.writeValueAsString(room);
    	        	   redisTemplate.opsForValue().set("room_"+String.valueOf(roomId), roomJson);
    	        	   response.setCode(1);
                       response.setMsg(String.valueOf(roomId));
    	           }else {
    	        	   response.setMsg("房间创建失败");
    	           }
	               sendMessage(response);
    	    	   log.info("操作步骤二");
    	    	   break;
    	    	 //进入房间
    	       case(3):
    	    	   //先在数据库中创建房间
    	    	   Response thisResponse = enterRoom(data.get("room_id").toString(), user);
	        	   sendMessage(thisResponse);
    	    	   break;
    	        //准备
    	       case(4):
    	    	   
    	    	   break;
    	    }
    	} catch (Exception e) {  
    		log.error("操作失败");
    	    e.printStackTrace();  
    	}  
    }
    
    /**
     *  方法3
     *  进入房间
     */
    public Response enterRoom(String roomId, Map user) {
    	Response response = new Response(0, "进入房间失败");
    	Room room  = (Room)redisTemplate.opsForValue().get("room_"+roomId);
    	//添加东家
 	    String userId = user.get("user_id").toString();
 	    String name  = user.get("nick").toString();
 	    String avatar  = user.get("avatar").toString();
    	int result = room.addClient(userId, name, avatar, 0);
    	if(result > 0) {
    		response.setCode(1);
    		response.setMsg("加入成功");
    	} else {
    		response.setMsg("房间人数超过限制");
    	}
    	return response;
    }
    
    /**
     * 方法4
     * 准备
     */
 
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
 
 
    public void sendMessage(Response response ) throws IOException {
    	ObjectMapper mapper = new ObjectMapper(); 
    	String message = mapper.writeValueAsString(response);
        this.session.getBasicRemote().sendText(message);
    }
 
 
    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
    	//log.info(message);
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                continue;
//            }
//        }
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

