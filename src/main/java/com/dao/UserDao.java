package com.dao;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tools.Dao;

public class UserDao extends Dao {
    
	
	public UserDao(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	/**
	 * 插入用户
	 */
	public int insert(String account, String password, String phone, String nick) {
		String sql = "insert into users(account, password, phone, nick) values(?, ?, ?, ?)";
		return jdbcTemplate.update(sql, account, password, phone, nick);
	}
	
	/**
	 * 通过账号判断用户是否存在
	 */
	public long userExsit(String account) {
		String sql = "SELECT count(user_id) FROM users where account='"+account+"'";
		long count = jdbcTemplate.queryForObject(sql, Long.class);
		return count;
	}
}
