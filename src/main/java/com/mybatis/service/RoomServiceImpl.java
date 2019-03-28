package com.mybatis.service;

import javax.annotation.Resource;

import com.mybatis.entity.Room;
import com.mybatis.mapper.RoomMapper;

public class RoomServiceImpl implements RoomService {
	
	@Resource
	private RoomMapper roomMapper;

	@Override
	public int insert(int userId, int amount, int people) {
		String content = "";
		Long time = System.currentTimeMillis() / 1000;
		Room room = new Room(userId, amount, people, content, time.intValue());
		int result = roomMapper.insert(room);
		int id = 0;
		if(result > 0) {
            id = room.getId();
		}
		return id;
	}

}
