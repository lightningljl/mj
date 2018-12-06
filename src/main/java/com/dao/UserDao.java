package com.dao;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.entity.Users;
import com.tools.Dao;

public class UserDao extends Dao {
    
	private String table = "users";
	
	public UserDao(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	/**
	 * 插入用户
	 */
	public int insert(String account, String password, String phone, String nick) {
		String sql = "insert into "+table+"(account, password, phone, nick) values(?, ?, ?, ?)";
		return jdbcTemplate.update(sql, account, password, phone, nick);
	}
	
	/**
	 * 通过账号判断用户是否存在
	 */
	public long userExsit(String account) {
		String sql = "SELECT count(user_id) FROM "+table+" where account='"+account+"'";
		long count = jdbcTemplate.queryForObject(sql, Long.class);
		return count;
	}
	
	/**
	 * 通过账号判断用户是否存在
	 */
	public Map login(String account, String password) {
		String sql = "SELECT user_id,account,nick,gender,avatar FROM "+table+" where account='"+account+"' and password='"+password+"'";
		Map  map = jdbcTemplate.queryForMap(sql);
		return map;
	}
}
