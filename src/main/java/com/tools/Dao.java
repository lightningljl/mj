package com.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库操作类
 * @author ljl
 *
 */
public class Dao {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	public Dao() {
		this.jdbcTemplate = new JdbcTemplate();
	}
	
	public JdbcTemplate getDao() {
		return this.jdbcTemplate;
	}
}
