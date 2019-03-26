package com.tools;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;

import com.entity.Room;

public class HomeSync {
	private RedisTemplate<String, Serializable> redisTemplate;
	
	public HomeSync( RedisTemplate<String, Serializable> redisObj ) {
		this.redisTemplate = redisObj;
	}
	
	public synchronized Room inquire( String roomId ) {
		Room room = (Room)redisTemplate.opsForValue().get("room_"+roomId);
		return room;
	}
}
