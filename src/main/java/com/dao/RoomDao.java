package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
	public int insert(String amount, String people) {
		String content = "";
		long time = System.currentTimeMillis() / 1000;
		String created_at = String.valueOf(time);
//		String sql = "insert into "+table+"(amount, content, created_at) values(:amount,:content,:created_at)";
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//    	parameters.addValue("amount", amount);
//    	parameters.addValue("content", content);
//    	parameters.addValue("created_at", created_at);
		String sql = "insert into "+table+"(amount, people, content, created_at) values("+amount+","+people+",'',"+created_at+")";
    	KeyHolder keyHolder = new GeneratedKeyHolder();
		int autoIncId = 0;  
		int result = jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
		if(result>0) {
			autoIncId = keyHolder.getKey().intValue();
		}
		return autoIncId;
	}
}
