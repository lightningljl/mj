package com.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
   *  手牌类
 */
public class Hands {
    /**
     * 没摸牌的初始化手牌
     */
	public int[][] brand = {{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
	/**
	 * 用户手牌，一纬
	 */
	public ArrayList<Integer> card;
	/**
	   * 用户出牌
	 */
	public ArrayList<Integer> play;
	
	/**
	  * 用户碰牌
	 */
	public ArrayList<Integer> touch;
	
	/**
	 * 用户杠牌
	 */
	public ArrayList<Integer> bar;
	
	/**
	 * 是否胡牌,0否，1是,默认0
	 */
	public int win = 0;
	
	/**
	   * 判定用户多少藩,默认平胡,1藩
	 */
	public int score = 1;
	
	/**
	 * 用来存储用户是否缺乔
	 */
	public Set<Integer> single;
	
	/**
	 * 存储三维牌型，存储进入redis的时候要清除
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> thisBrands;
}
