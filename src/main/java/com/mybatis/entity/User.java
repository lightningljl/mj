package com.mybatis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	//用户ID
    private int userId;
    //用户登录账号
    private String account;
    //用户密码
    private String password;
    //用户手机
    private String phone;
    //用户昵称
    private String nick;
    //用户性别
    private int gender;
    //用户头像
    private String avatar;
}
