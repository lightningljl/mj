package com.validate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class Regist {
	@NotBlank(message = "手机号码格式不正确")
	@Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}$",message = "手机号码格式不正确")
    public String Account;
	
	@NotBlank(message = "密码格式不正确")
	public String Password;

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
	
}
