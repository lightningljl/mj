package com.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import com.mybatis.entity.Room;

public interface RoomMapper {
	int insert(@Param("room")Room room);
}
