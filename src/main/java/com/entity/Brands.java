package com.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
     * @return 默认0，没有胡牌，1代表胡牌了，1翻，以此类推
     */
    public int win(int unit, Hands info) {
    	int cate = 3;
    	//将还在手中的牌转为二维数组
    	//0代表筒，1代表条，2代表万
    	int number = info.play.size();
    	ArrayList[] thisBrands = new ArrayList[cate];
    	//初始化
    	for(int j=0; j<number; j++) {
    		thisBrands[j] = new ArrayList();
    	}
    	//二维化数据结构，类似
    	//筒{1,2,2,3,4}
    	//条{2,3,3,4,5,6}
    	//万{6,7,8,9,9}
    	Set single = new HashSet();
    	for(int i=0; i<number; i++) {
    		int type = (int) Math.floor(info.play.get(i)/36);
    		int value = info.play.get(i)%36;
    		int digit = (int) Math.ceil(value/4);
    		thisBrands[type].add(digit);
    		single.add(type);
    	}
    	//判断是否缺桥
    	if(single.size() == 3) {
    		return 0;
    	}
    	//继续判定用户手牌数量，如果为1，则判断新牌和手牌一样，则胡牌
    	if(number == 1 && info.play.get(0) == unit) {
    		//进一步判断番数
    		return calc(unit, info);
    	}
    	//找到手牌中的将牌，然后判断
    	
    	return 0;
    }
    
    /**
                 * 胡牌后，判断有多少翻
     * @param unit
     * @param info
     * @return
     */
    public int calc(int unit, Hands info) {
    	return 1;
    }
}
