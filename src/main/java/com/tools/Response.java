package com.tools;

import java.util.ArrayList;

/**
 * 统一返回给前端的类
 */
public class Response {
	
	//返回前端的编码
    private int code;
    //返回前端的消息
    private String msg;
    //返回前端的数据集合
    private ArrayList data;
    
    public Response(int code, String msg) {
    	this.code = code;
    	this.msg  = msg;
    }
    
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList getData() {
		return data;
	}
	public void setData(ArrayList data) {
		this.data = data;
	}
    
   
    
}
