package com.dao;
import com.tools.Dao;
import com.entity.Users;

public class UserDao extends Dao {
    
	/**
	 * 插入用户
	 */
	public int insert(String account, String password, String phone, String nick) {
		String sql = "insert into users(account, password, phone, nick) values(?, ?, ?, ?)";
		return this.getDao().update(sql, account, password, phone, nick);
	}
	
	/**
	 * 通过账号判断用户是否存在
	 */
	public int userExsit(String account) {
		int number = 0;
		return number;
	}
}
