package com.tools;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

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
      	   User user = userService.inquire(Integer.valueOf(uid));
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
      	   storage(room);
      	   //将房间信息存入redis,并跳转
      	   response.setCode(1);
           response.setMsg(String.valueOf(roomId));
         }
         return response;
	}
	
	public synchronized Response enterRoom(String roomId, Map user) {
		Response response = new Response(0, "进入房间失败");
    	Room room  = (Room)redisTemplate.opsForValue().get("room_"+roomId);
    	//添加东家
 	    String userId = user.get("user_id").toString();
 	    String name  = user.get("nick").toString();
 	    String avatar  = user.get("avatar").toString();
    	int result = room.addClient(userId, name, avatar, 0);
    	if(result > 0) {
    		//存储房间
    		storage(room);
    		response.setCode(1);
    		response.setMsg("加入成功");
    	} else {
    		response.setMsg("房间人数超过限制");
    	}
    	return response;
	}
	
	/**
                 * 存储房间
     */
    public void storage(Room room) {
    	 ObjectMapper mapper = new ObjectMapper();
    	 String roomJson = "";
		 try {
			 roomJson = mapper.writeValueAsString(room);
			 redisTemplate.opsForValue().set("room_"+String.valueOf(room.id), roomJson);
		 } catch (JsonProcessingException e) {
			 System.out.println("房间信息存储失败"+roomJson);
			 e.printStackTrace();
		 }
    }
}
