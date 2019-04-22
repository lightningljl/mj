package com.entity;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
	public String uid;
	
	/**
     * 是否准备充分
     */
	public int ready=0;
	
	/**
	 * 用户昵称
	 */
	public String nickName;
	
	/**
	 * 用户头像
	 */
	public String avatar;
	
	/**
	 * 用户金钱
	 */
	public float amount = 0f;
	
	/**
	 * 是否庄家,0否,1是
	 */
	public int master = 0;
	
	/*
	 * 用户的排序
	 */
	public int sort = 0;
	
	public Player() {
		
	}
	
	/**
	 * 
	 * @param thisUid
	 * @param name
	 * @param avatar
	 * @param master
	 */
	public Player(String thisUid, String name, String avatar, int master) {
		uid = thisUid;
		nickName = name;
		this.avatar = avatar;
		this.master = master;
	}
}
