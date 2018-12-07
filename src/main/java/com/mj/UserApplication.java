package com.mj;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import com.dao.ActionLogDao;
import com.dao.UserDao;
import com.tools.Tools;

@RestController
public class UserApplication {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public HttpSession session;
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
        	//插入操作记录
        	ActionLogDao model = new ActionLogDao(jdbcTemplate);
        	model.insert(1, "账号:"+regist.Account+",注册成为新用户");
        	response.setCode(1);
        	response.setMsg("注册成功");
        }
	    return response;
	}
	
	@RequestMapping(value="/user/login", produces = MediaType.APPLICATION_JSON_VALUE ,method= RequestMethod.POST)
	public Response login(@Valid Regist regist, BindingResult bindingResult) {
		Response response = new Response(0, "错误的账号和密码组合");
		//参数校验，返货错误信息
        if(bindingResult.hasErrors()) {
        	logger.info("账号校验出错,登录账号:"+regist.Account);;
        	response.setMsg( bindingResult.getFieldError().getDefaultMessage() );
        	return response;
        }
        //进行用户信息插入
        UserDao dao = new UserDao(jdbcTemplate);
        long number = dao.userExsit(regist.Account);
        //如果用户已经存在，则提示
        if(number==0) {
        	response.setMsg("用户账号未注册");
        	return response;
        }
        Map map = dao.login(regist.Account, regist.Password);
        //如果当前账号密码未查询到相关数据
        if( map.isEmpty() ) {
        	return response;
        }
        //写入session,TODO
        session.setAttribute("user", map);
        //写入日志
        ActionLogDao model = new ActionLogDao(jdbcTemplate);
    	model.insert(2, "账号:"+regist.Account+",进行登录操作");
    	//返回前端登录成功
    	Map user = (Map)session.getAttribute("user");
    	response.setCode(1);
    	response.setMsg("登录成功"+user.get("account").toString());
		return response;
	}
	
	/**
	 * 测试接口
	 * @return
	 */
	@RequestMapping(value="/user/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response teset(){
		Map user = (Map)session.getAttribute("user");
		Response response = new Response(0, user.get("user_id").toString());
	    return response;
	}
}
