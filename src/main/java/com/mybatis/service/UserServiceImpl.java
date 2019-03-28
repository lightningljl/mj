package com.mybatis.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mybatis.entity.User;
import com.mybatis.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper userMapper;
	
	@Override
	public User inquire(int id) {
		return userMapper.inquire(id);
	}
}
