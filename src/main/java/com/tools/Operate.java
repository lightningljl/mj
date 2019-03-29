package com.tools;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.core.RedisTemplate;

import com.dao.RoomDao;
import com.entity.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatis.entity.User;
import com.mybatis.service.RechargeService;
import com.mybatis.service.RoomService;
import com.mybatis.service.UserService;

public class Operate {
	public RedisTemplate<String, Serializable> redisTemplate;
	@Resource
    private RoomService roomService;
	@Resource
    private UserService userService;
	
	//使用redission锁
	private RedissonClient client;
	
	public Operate() {
		Config config = new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");
        client = Redisson.create(config);
	}
	
	public Response initUser(String uid) {
		//从数据库中查询用户信息，存储到redis中去
		Response response = new Response(1, "用户信息存储成功");
		//用户信息查询
   	    User user = userService.inquire(Integer.valueOf(uid));
   	    storageUser(user);
   	    return response;
	}
	
	/**
	 * 创建房间
	 * @param data
	 * @param uid
	 * @return
	 */
	public Response create(Map data, String uid) {
         //获取单场的基础价格
         int amount = Integer.valueOf( data.get("amount").toString() );
         int people = Integer.valueOf( data.get("people").toString() );
         //通过金额和人数来创建房间
         int roomId = roomService.insert(Integer.valueOf(uid), amount, people);
         Response response = new Response(0, "房间创建失败");
         if(roomId > 0) {
      	   //进行房间初始化,将当前用户加入房间
      	   Room room = new Room(roomId, people);
      	   //用户信息查询
      	   User user = getUser(uid);
      	   //添加东家
      	   String userId =String.valueOf( user.getUserId() ) ;
      	   String name  = user.getNick();
      	   String avatar  = user.getAvatar();
      	   int enterRoom = room.addClient(userId, name, avatar, 1);
      	   if(enterRoom == 0) {
      		   response.setMsg("人数超过限制，加入失败");
      		   return response;
      	   } 
      	   //存储room信息
      	   storageRoom(room);
      	   //将房间信息存入redis,并重新存储
      	   user.setRoomId(String.valueOf(roomId));
      	   storageUser(user);
      	   //将用户所在的房间号放进redis
      	   redisTemplate.opsForValue().set("room_"+String.valueOf(room.id), String.valueOf(roomId) );
      	   response.setCode(1);
           response.setMsg(String.valueOf(roomId));
         }
         return response;
	}
	
	/**
	 * 进入房间操作
	 * @param roomId
	 * @param user
	 * @return
	 */
	public Response enterRoom(String roomId, Map user) {
		Response response = new Response(0, "进入房间失败");
		//针对房间进行行锁
		RLock lock = client.getLock("room_lock_"+roomId);
		lock.lock();
		//执行业务
    	Room room  = (Room)redisTemplate.opsForValue().get("room_"+roomId);
    	//添加东家
 	    String userId = user.get("user_id").toString();
 	    String name  = user.get("nick").toString();
 	    String avatar  = user.get("avatar").toString();
    	int result = room.addClient(userId, name, avatar, 0);
    	if(result > 0) {
    		//存储房间
    		storageRoom(room);
    		response.setCode(1);
    		response.setMsg("加入成功");
    	} else {
    		response.setMsg("房间人数超过限制");
    	}
    	//执行完毕，释放锁
    	lock.unlock();
    	return response;
	}
	
	/**
     * 存储房间
     */
    public void storageRoom(Room room) {
    	 ObjectMapper mapper = new ObjectMapper();
    	 String roomJson = "";
		 try {
			 roomJson = mapper.writeValueAsString(room);
			 redisTemplate.opsForValue().set("room_"+String.valueOf(room.id), roomJson);
		 } catch (JsonProcessingException e) {
			 System.out.println("房间信息存储失败:"+roomJson);
			 e.printStackTrace();
		 }
    }
    
    /**
     * 用户存储
     */
    public void storageUser(User user) {
    	ObjectMapper mapper = new ObjectMapper();
   	     String userJson = "";
		 try {
			 userJson = mapper.writeValueAsString(user);
			 redisTemplate.opsForValue().set("user_"+String.valueOf(user.getUserId()), userJson);
		 } catch (JsonProcessingException e) {
			 System.out.println("用户信息存储失败:"+userJson);
			 e.printStackTrace();
		 }
    }
    
    /**
     * 获取用户信息
     */
    public User getUser(String uid) {
    	User user  = (User)redisTemplate.opsForValue().get("user_"+uid);
    	return user;
    }
}
