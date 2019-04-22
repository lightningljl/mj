package com.mj;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;
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

import com.tools.JacksonUtil;
import com.tools.Response;
import com.validate.Regist;
import com.dao.ActionLogDao;
import com.dao.UserDao;
import com.entity.Room;
import com.mj.service.Operate;
import com.tools.Tools;

@RestController
public class UserApplication {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public HttpSession session;
	@Resource
	private Operate operate;
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
        if( map==null || map.isEmpty() ) {
        	return response;
        }
        //写入session,TODO
        session.setAttribute("user", map);
        //写入日志
        ActionLogDao model = new ActionLogDao(jdbcTemplate);
    	model.insert(2, "账号:"+regist.Account+",进行登录操作");
    	//返回前端登录成功
    	//Map user = (Map)session.getAttribute("user");
    	//String account = user.get("account").toString();
    	operate.initUser(map.get("user_id").toString());
    	response.setCode(1);
    	response.setMsg("登录成功");
		return response;
	}
	
	@RequestMapping(value="/user/uuid", produces = MediaType.APPLICATION_JSON_VALUE,method= RequestMethod.POST)
	public Response uuid() {
		Response response = new Response(0, "您还未登录");
		Map user = (Map)session.getAttribute("user");
		if( user!=null && !user.isEmpty() ) {
			Map<String, String> info = new HashMap<>();
			info.put("uuid", user.get("user_id").toString());
			response.setCode(1);
			response.setMsg("成功");
			response.setData(info);
		}
		return response;
	}
	
	/**
	 * 测试接口
	 * @return
	 */
	@RequestMapping(value="/user/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response teset(){
		String roomJson = "{\"id\":6,\"brands\":{\"mj\":[[4,4,4,4,4,4,4,4,4],[4,4,4,4,4,4,4,4,4],[4,4,4,4,4,4,4,4,4]],\"total\":108,\"leave\":null,\"cate\":3,\"hands\":null},\"money\":null,\"people\":3,\"amount\":0,\"readerNum\":0,\"status\":0,\"config\":null,\"calcNumber\":1,\"playerList\":[{\"uid\":\"1\",\"ready\":0,\"nickName\":\"y3m3va\",\"avatar\":\"http://www.zuijiabianshou.com/public/upload/1286a526-4003-4873-b9e0-d31f987e7273.png\",\"amount\":0.0,\"master\":1,\"sort\":0}]}";
		Room room = JacksonUtil.readValue(roomJson, Room.class);
		System.out.println(room.people);
//		ExecutorService pool = Executors.newCachedThreadPool();
//		Callable c1 = new ThreadBoy(jdbcTemplate); 
//		Callable c2 = new ThreadBoy(jdbcTemplate);
//		Callable c3 = new ThreadBoy(jdbcTemplate);
//		Callable c4 = new ThreadBoy(jdbcTemplate);
//		Callable c5 = new ThreadBoy(jdbcTemplate);
//		Future<Map> f1 = pool.submit(c1);
//		Future<Map> f2 = pool.submit(c2);
//		Future<Map> f3 = pool.submit(c3);
//		Future<Map> f4 = pool.submit(c4);
//		Future<Map> f5 = pool.submit(c5);
		Response response = new Response(0, "成功");
//		try {
//			Map userInfo = f1.get();
//			System.out.println(userInfo.toString());
//			response.setData(userInfo);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		UserDao dao = new UserDao(jdbcTemplate);
//        Map map1 = dao.inquire("1");
//        Map map2 = dao.inquire("2");
//        Map map3 = dao.inquire("3");
//        Map map4 = dao.inquire("4");
//        Map map5 = dao.inquire("5");
//        System.out.println(map1.toString());
//		response.setData(map1);
	    return response;
	}
}




class ThreadBoy implements Callable<Map>{
	
	private JdbcTemplate jdbcTemplate;
	
	public ThreadBoy( JdbcTemplate obj ) {
		jdbcTemplate = obj;
	}
	
    @Override
    public Map call() throws Exception {
    	UserDao dao = new UserDao(jdbcTemplate);
        Map map = dao.inquire("1");
        return map;
    }
}
