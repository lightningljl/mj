package com.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	//房间ID
	public int id;
	//初始化手牌
	public Brands brands;
	//标记用户信息
	public Player[] player;
    //标记用户当前的money,初始化时，每个用户都需要上交
    public int[] money;
    //初始化用户数量
    public int people;
    //初始化基础金额，一番
    public int amount;
    //初始化准备数量
    public int readerNum = 0;
    //当前房间状态，0等待状态,1打牌当中
    public int status = 0;
    //各项配置
    public HashMap<String, String> config;
    //当前进入人的数量
    public int calcNumber = 0;
    //给房价追加用户
    public Room(int id, int pnumber) {
    	this.id = id;
    	brands = new Brands();
    	player = new Player[pnumber];
    	people = pnumber;
    }
    
    //准备
    public int reader(int key) {
    	int canStart = 0;
    	player[key].ready = 1;
    	readerNum++;
    	if(readerNum == people) {
    		canStart = 1;
    	}
    	return canStart;
    }
    
    //进入房间,创建房间的人会第一个进入，因此庄家的ID永远为0
    public int addClient(String uid, String name, String avatar, int master) {
    	Player newPlayer = new Player(uid, name, avatar, master);
    	//判断当前用户数量
    	System.out.println("当前用户数量:"+String.valueOf(calcNumber));
    	//如果达到的上限值
    	if(calcNumber>=people) {
    		return 0;
    	}
    	newPlayer.sort = calcNumber;
    	player[calcNumber] = newPlayer;
    	calcNumber++;
    	return 1;
    }
    
    /**
     * 通过用户ID返回当前用户
     */
    public Player inquirePlayer( String userId ) {
    	Player user = null;
    	for(int i=0;i<calcNumber;i++){
    		if(player[i].uid.equals(userId)) {
    			user = player[i];
    			break;
    		}
    	}
    	return user;
    }
    
    /**
     * 设置用户数据
     */
    public void setPlayer(Player user) {
    	int key = user.sort;
    	player[key] = user;
    }
    
    /**
     * 开牌，初始化，
     */
    public int licensing() {
    	//如果准备人数和玩家人数不匹配，则失败
    	if(readerNum!=people) {
    		return 0;
    	}
    	//获取用户ID
    	String[] uidList = new String[people];
    	for(int i=0;i<people;i++) {
    		uidList[i] = player[i].uid;
    	}
    	brands.init(people, uidList);
    	return 1;
    }
}
