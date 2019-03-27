package com.tools;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.entity.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Operate {
	public RedisTemplate<String, Serializable> redisTemplate;
	
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
