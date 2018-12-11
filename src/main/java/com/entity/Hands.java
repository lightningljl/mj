package com.entity;

/**
   *  手牌类
 */
public class Hands {
    /**
                 * 没摸牌的初始化手牌
     */
	public int[][] brand = {{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
	
	/**
	   * 用户出牌
	 */
	public int[][] play = {};
	
	/**
	  * 用户碰牌
	 */
	public int[] touch = {};
	
	/**
	 * 用户杠牌
	 */
	public int[] bar = {};
	
	/**
	 * 是否胡牌,0否，1是,默认0
	 */
	public int win = 0;
	
	/**
	   * 判定用户多少藩,默认平胡,1藩
	 */
	public int score = 1;
}
