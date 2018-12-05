package com.tools;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库操作类
 * @author ljl
 *
 */
public class Dao {
	
	public JdbcTemplate jdbcTemplate;
	
	public Dao(JdbcTemplate jdbcTemplate) {
	    this.jdbcTemplate = jdbcTemplate;	
	}
}
