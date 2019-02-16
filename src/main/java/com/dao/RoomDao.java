package com.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tools.Dao;

public class RoomDao extends Dao {
	private String table = "room";
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public RoomDao(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}
	
	/**
	 * 插入房间
	 */
	public int insert(String amount) {
		String content = "";
		long time = System.currentTimeMillis() / 1000;
		String createdAt = String.valueOf(time);
		String sql = "insert into "+table+"(amount, content, created_at) values(?, ?, ?)";
		return jdbcTemplate.update(sql, amount, content, createdAt);
	}
}
