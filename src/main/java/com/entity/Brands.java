package com.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
   * 手牌模型
 */
public class Brands {
    /**
     * 定义麻将数据类型，二位
     *0代表筒,1代表条,2代表万
     */
	public int[][] mj = {{4,4,4,4,4,4,4,4,4},{4,4,4,4,4,4,4,4,4},{4,4,4,4,4,4,4,4,4}};
	
    //定义牌的数量
	public int total = 108;
	
	//定义剩下的牌的数组，初始化时108
	public List<Integer> leave;
	
	//定义用户手牌
	//public Hands[] hands;
	
	//定义用户手牌
	public Map<String, Hands> hands;
	
	/**
	 * 初始化牌，庄家14张，贤家13张
	 * @param number int 有多少用户
	 */
	public void init(int number, String[] uidList) {
		hands = new HashMap<>();
		//打乱顺序,随机108张牌，模拟洗牌
		getSequence(total);
		for( int i=0; i<number; i++ ) {
			//第一个人摸14张排，第一个以后的人摸13张排
			int iniNumber = i== 0? 14 : 13;
			//默认0是庄家
			Hands unit = touch(i, iniNumber);
			hands.put(uidList[i], unit);
		}
	}
	
	/**
	 * 摸牌方法
	 * @param int key 第几个用户
	 * @param int number 就是摸几张牌
	 */
	public Hands touch(int key, int number) {
		Hands unit = new Hands();
		for( int i=0; i<number; i++ ) {
			unit.card.add(leave.get(i));
			leave.remove(i);
		}
		return unit;
	}
	
	/**
	 * 随机生成一定数量的牌，模拟洗牌
	 * @param no int 生成数量
	 */
    public void getSequence(int no) {
    	leave = new ArrayList<Integer>(no);
        for(int i = 0; i < no; i++){
        	leave.add(i);
        }
        //洗牌
        Collections.shuffle(leave);
        //多洗一次
        Collections.shuffle(leave);
    }
    
    /**
     * 出牌
     * @param unit
     */
    public void play(int unit,String uid) {
    	//用户的手牌减少,出牌多处
    	Hands thisHands = hands.get(uid);
    	for( int i = 0 ; i < thisHands.card.size() ; i++) {
    		//如果找到牌后，进行删除
    		if(thisHands.card.get(i).intValue() == unit) {
    			thisHands.card.remove(i);
    			break;
    		}
    	}
    	//出牌增加
    	thisHands.play.add(unit);
    }
    
    /**
     * 
     * @param unit 牌号
     * @param info 用户牌
     * @return
     */
    public boolean win(int unit, Hands info) {
    	//将还在手中的牌转为二维数组
    	//0代表筒，1代表条，2代表万
    	int number = info.play.size();
    	int[][] thisBrands = new int[number][2];
    	for(int i=0; i<number; i++) {
    		int type = (int) Math.floor(info.play.get(i)/36);
    		int value = info.play.get(i)%36;
    		
    	}
    	return false;
    }
}
