package com.mj;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tools.Response;
import com.validate.Regist;
import com.dao.UserDao;
import com.tools.Tools;

@RestController
public class UserApplication {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 用户注册接口
	 * @return
	 */
	@RequestMapping(value="/user/regist", produces = MediaType.APPLICATION_JSON_VALUE ,method= RequestMethod.POST)
	public Response regist(@Valid Regist regist, BindingResult bindingResult ){
		Response response = new Response(0, "注册失败");
		//参数校验，返货错误信息
        if(bindingResult.hasErrors()) {
        	logger.info("注册账号:"+regist.Account);;
        	response.setMsg( bindingResult.getFieldError().getDefaultMessage() );
        	return response;
        }
        //进行用户信息插入
        UserDao dao = new UserDao(jdbcTemplate);
        long number = dao.userExsit(regist.Account);
        //如果用户已经存在，则提示
        if(number>0) {
        	response.setMsg("该手机号码已注册，请直接登录");
        	return response;
        }
        int result = dao.insert(regist.Account, regist.Password, regist.Account, Tools.random(6));
        //如果插入成功，则提示
        if(result > 0) {
        	response.setCode(1);
        	response.setMsg("注册成功");
        }
	    return response;
	}
	
	/**
	 * 测试接口
	 * @return
	 */
	@RequestMapping(value="/user/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response teset(){
		Response response = new Response(0, Tools.getMD5("123456"));
	    return response;
	}
}
