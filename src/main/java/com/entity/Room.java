package com.entity;

import java.util.HashMap;
import java.util.Map;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Room {
	//房间ID
	public int id;
	//初始化手牌
	public Brands brands;
	//标记用户ID
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
    	int number = player.length;
    	//如果达到的上限值
    	if(number>=people) {
    		return 0;
    	}
    	player[number] = newPlayer;
    	return 1;
    }
    
    /**
                  * 开牌，初始化，
     */
    public int licensing() {
    	//如果准备人数和玩家人数不匹配，则失败
    	if(readerNum!=people) {
    		return 0;
    	}
    	brands.init(people);
    	return 1;
    }
}
