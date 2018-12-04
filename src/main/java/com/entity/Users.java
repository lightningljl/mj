package com.entity;

/**
 * 用户表，记录用户信息等
 * @author ljl
 *
 */
public class Users {
	//用户ID
    private int User_id;
    //用户登录账号
    private String Account;
    //用户密码
    private String Password;
    //用户手机
    private String Phone;
    //用户昵称
    private String Nick;
    //用户性别
    private int Gender;
    //用户头像
    private String Avatar;
	public int getUser_id() {
		return User_id;
	}
	public void setUser_id(int user_id) {
		User_id = user_id;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getNick() {
		return Nick;
	}
	public void setNick(String nick) {
		Nick = nick;
	}
	public int getGender() {
		return Gender;
	}
	public void setGender(int gender) {
		Gender = gender;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
	}
    
    
}
