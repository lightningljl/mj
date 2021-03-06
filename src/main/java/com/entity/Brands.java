package com.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	
	//定义麻将维度
	public int cate = 3;
	
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
			Hands unit = initTouch(i, iniNumber);
			hands.put(uidList[i], unit);
		}
	}
	
	/**
	 * 初始摸牌方法
	 * @param int key 第几个用户
	 * @param int number 就是摸几张牌
	 */
	public Hands initTouch(int key, int number) {
		Hands unit = new Hands();
		for( int i=0; i<number; i++ ) {
			unit.card.add(leave.get(i));
			leave.remove(i);
		}
		return unit;
	}
	
	/**
	 * 指定用户获取一张牌,摸牌
	 * @param uid
	 * @return
	 */
	public int gain(String uid) {
		int number = leave.get(0);
		leave.remove(0);
		Hands thisHands = hands.get(uid);
		thisHands.card.add(number);
		return number;
	}
	
	/**
	 * 用户碰牌
	 * @return
	 */
	public boolean touch(int unit, Hands info) {
		//将牌转为二维的
		int[] newUnit = transferUnit( unit );
		int number = checkTouch(newUnit, info);
		if(number>1) {
			//进行碰牌操作
			//用户碰牌增加，手牌减少2个
			info.touch.add(unit);
			//反查基数
			int basic = newUnit[0]*36+newUnit[1]*4;
			//开始进行删除
			int calc = 0;
			for(int i=0; i<info.card.size(); i++) {
			    if(info.card.get(i)>=basic) {
			    	calc++;
			    	info.card.remove(i);
			    	if(calc==2) {
			    		break;
			    	}
			    }	
			}
			return true;
		}
		return false;
	}
	
	
	public int checkTouch(int[] unit, Hands info) {
		HashMap<Integer, HashMap<Integer, Integer>> thisBrands = info.thisBrands;
		//判定是否有该手牌
		if(info.thisBrands.get(unit[0]).containsKey(unit[1]) ) {
			int number = info.thisBrands.get(unit[0]).get(unit[1]);
			//如果这种牌型有2个或者两个以上，则可以碰或者
			return number;
		}
		return 0;
	}
	
	
	/**
	 * 用户杠牌
	 * @return
	 */
	public boolean bar(int unit, Hands info) {
		//将牌转为二维的
		int[] newUnit = transferUnit( unit );
		int number = checkTouch(newUnit, info);
		if(number>2) {
			info.bar.add(unit);
			//反查基数
			int basic = newUnit[0]*36+newUnit[1]*4;
			//开始进行删除
			int calc = 0;
			for(int i=0; i<info.card.size(); i++) {
			    if(info.card.get(i)>=basic) {
			    	calc++;
			    	info.card.remove(i);
			    	if(calc==3) {
			    		break;
			    	}
			    }	
			}
			return true;
		}
		return false;
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
     * 用户摸牌，返回牌
     * @param uid
     * @return
     */
    public int getCard(String uid) {
    	int unit = leave.get(0);
    	leave.remove(0);
    	hands.get(uid).card.add(unit);
    	return unit;
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
    	//将还在手中的牌转为二维数组
    	//0代表筒，1代表条，2代表万
    	int number = info.play.size();
    	//ArrayList<Integer>[] thisBrands = new ArrayList[cate];
    	transfer(info);
    	HashMap<Integer, HashMap<Integer, Integer>> thisBrands = info.thisBrands;
    	Set<Integer> single = info.single;
    	//判断是否缺桥
    	if(single.size() == 3) {
    		return 0;
    	}

    	//记录将牌牌型
    	single.clear();
    	//继续判定用户手牌数量，如果为1，则判断新牌和手牌一样，则胡牌
    	if(number == 1 && info.play.get(0) == unit) {
    		//进一步判断番数
    		return calc(unit, info);
    	}
    	//找到手牌中的将牌，然后判断
    	//先找到所有可能的将牌,拿出来，然后依次判断
    	int length = 0;
    	int win = 0;
    	int calc   = 0;
    	HashMap<Integer, HashMap<Integer, Integer>> copy = null;
    	for(int i = 0; i<cate; i++) {
    		length = thisBrands.get(i).size();
    		//先去掉缺桥，然后直接进入下一层
    		if(length == 0) {
    			continue;
    		}
    		for(int key:thisBrands.get(i).keySet()) {
    			calc = thisBrands.get(i).get(key);
    			//先剔除两张将牌
    			if( calc > 1  ) {
    				calc = calc -2 ;
    				copy = (HashMap<Integer, HashMap<Integer, Integer>>) thisBrands.clone();
    				copy.get(i).put(key, calc);
    				//胡牌判断,判断是否满足n*ABC+k*DDD这种形式，因为将牌已经去除
    				win = checkBrands(copy, cate);
    				if(win > 0) {
    					return win;
    				}
    			}
            }
    	}
    	return 0;
    }
    
    /**
     * 剔除将牌后的手牌，进一步判断是否胡牌
     * 判断是否满足n*ABC+k*DDD这种格式
     * @param thisBrands
     * @return
     */
    public int checkBrands(HashMap<Integer, HashMap<Integer, Integer>> thisBrands, int cate) {
    	int length = 0;
    	//计算执行次数，最多有12张牌，所以最多循环4次
    	int times = 4;
    	int detail = 0;
    	for(int i = 0; i<cate; i++) {
    		length = thisBrands.get(i).size();
    		//先去掉缺桥，然后直接进入下一层
    		if(length == 0) {
    			continue;
    		}
    		//求连续的3个，即n*ABC,并消除
    		for(int j=0; j<times; j++) {
    			loop:for(int key:thisBrands.get(i).keySet()) {
    				if(thisBrands.get(i).containsKey(key+1) && thisBrands.get(i).containsKey(key+2) ) {
    					//连续将ABC模式的值从数组中剔除
    					for(int k= 0; k<3; k++) {
    						detail = thisBrands.get(i).get(key+k);
    						if(detail < 1) {
        						thisBrands.get(i).remove(key);
        					} else {
        						thisBrands.get(i).put(key, detail);
        					}
    					}
    					break loop;
    				}
        		}
    		}
    		//继续求n*AAA这种数据
    		for (Iterator<Map.Entry<Integer, Integer>> it = thisBrands.get(i).entrySet().iterator(); it.hasNext();){
    		    Map.Entry<Integer, Integer> item = it.next();
    		    if(item.getValue().equals(3)) {
    		    	it.remove();
    		    }
    		}
    		//最终判定数量是否为0;
    		if(thisBrands.get(i).size() != 0) {
    			return 0;
    		}
    	}
    	return 1;
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
    
    /**
     * 将一维的麻将转为三维的，方便计算
     * @param info
     * @return
     */
    public void transfer(Hands info) {
    	//将还在手中的牌转为二维数组
    	//0代表筒，1代表条，2代表万
    	int number = info.play.size();
    	HashMap<Integer, HashMap<Integer, Integer>> thisBrands = new HashMap<Integer, HashMap<Integer, Integer>>();
    	for(int i=0; i<cate; i++) {
    		HashMap<Integer, Integer> thisUnit = new HashMap<>();
    		thisBrands.put(i, thisUnit );
    	}
    	//二维化数据结构，类似
    	//筒{1,2,2,3,4}
    	//条{2,3,3,4,5,6}
    	//万{6,7,8,9,9}
    	Set<Integer> single = new HashSet<>();
    	int calc   = 0;
    	for(int i=0; i<number; i++) {
    		int type = (int) Math.floor(info.play.get(i)/36);
    		int value = info.play.get(i)%36;
    		int digit = (int) Math.ceil(value/4);
    		calc = 1;
    		if(thisBrands.get(type).containsKey(digit)) {
    			calc = thisBrands.get(type).get(digit);
    			calc++;
    		}
    		thisBrands.get(type).put(digit, calc);
    		single.add(type);
    	}
    	info.single = single;
    	info.thisBrands = thisBrands;
    }
    
    /**
     * 单个牌转换
     * @param unit
     * @return
     */
    public int[] transferUnit( int unit ) {
    	int[] result = new int[2];
    	result[0] = (int) Math.floor(unit/36);
		int value = unit%36;
		result[1] = (int) Math.ceil(value/4);
		return result;
    }
}
