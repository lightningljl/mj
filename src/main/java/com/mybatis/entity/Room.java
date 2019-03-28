package com.mybatis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int masterId;
	private int amount;
	private int people;
	private String content;
	private int createdAt;
	
	public Room(int masterId, int amount, int people, String content, int createdAt) {
		this.masterId = masterId;
		this.amount = amount;
		this.people = people;
		this.content = content;
		this.createdAt = createdAt;
	}
}
