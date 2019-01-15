package com.entity;

import java.util.HashMap;

public class Room {
	//房间ID
	public int id;
	//标记用户ID
    public String[] player;
    //标记用户当前的money,初始化时，每个用户都需要上交
    public int[] money;
    //初始化用户数量
    public int number;
    //初始化积分纬度
    
    //各项配置
    public HashMap<String, String> config;
    //给房价追加用户
    public Room(int id) {
    	this.id = id;
    }
}
