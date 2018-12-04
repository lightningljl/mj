package com.entity;

/**
 * 用户行为记录表，例如注册时间，登录时间等
 * @author ljl
 *
 */
public class AccountLog {
	//日志ID
    private long Log_id;
    //动作类型
    private int Action;
    //动作描述
    private String Content;
    //创建时间
    private int Created_at;
	public long getLog_id() {
		return Log_id;
	}
	public void setLog_id(long log_id) {
		Log_id = log_id;
	}
	public int getAction() {
		return Action;
	}
	public void setAction(int action) {
		Action = action;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public int getCreated_at() {
		return Created_at;
	}
	public void setCreated_at(int created_at) {
		Created_at = created_at;
	}
    
    
}
