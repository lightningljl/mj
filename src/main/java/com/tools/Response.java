package com.tools;


import java.util.Map;

/**
 * 统一返回给前端的类
 */
public class Response {
	
	//返回前端的编码
    private int code;
    //返回前端的消息
    private String msg;
    //返回前端的数据集合
    private Map data;
    //返回的方法ID
    private String fid;
    
    public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Response(int code, String msg) {
    	this.code = code;
    	this.msg  = msg;
    	this.fid = "0";
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
	public Map getData() {
		return data;
	}
	public void setData(Map data) {
		this.data = data;
	}
    
   
    
}
