package com.dao;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tools.Dao;
public class ActionLogDao extends Dao {
	
	private String table="action_log";

	public ActionLogDao(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 插入操作记录
	 */
	public int insert( int action, String content ) {
		String createdAt = Long.toString(System.currentTimeMillis() / 1000);
		String sql ="INSERT INTO "+table+" (action,content,created_at) VALUES (?,?,?)" ;
		int result = jdbcTemplate.update(sql, action, content, createdAt);
		return result;
	}

}
