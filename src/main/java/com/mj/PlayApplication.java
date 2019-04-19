package com.mj;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mj.service.Operate;
import com.mybatis.service.RoomService;
import com.mybatis.service.UserService;
import com.tools.Response;

@Controller
public class PlayApplication {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public HttpSession session;
	
	@Resource
	private Operate operate;
	
	/**
	 * game home page
	 * @return
	 */
	@GetMapping(value="/play/index")
	public String index() {
		return "page/play/index";
	}
	
	/**
	 * login page
	 */
	@GetMapping(value="/play/login")
	public String login() {
		return "page/play/login";
	}
	
	/**
	 * 玩的页面
	 */
	@GetMapping(value="/play/play")
	public String play() {
		return "page/play/play";
	}
	
	/**
	  * 创建房间接口
	 * @param play 多少局
	 * @param way  玩法
	 * @param fan 番数限制
	 * @return Response
	 */
	@ResponseBody
	@RequestMapping(value="/play/room", produces = MediaType.APPLICATION_JSON_VALUE,method= RequestMethod.POST)
	public Response room(String play, String way, String fan) {
		Map user = (Map)session.getAttribute("user");
		Map<String,String> data = new HashMap<>();
		data.put("amount", "1");
		data.put("people", "3");
		data.put("play", play);
		data.put("way", way);
		data.put("fan", fan);
		log.info("用户"+user.get("user_id").toString()+"创建房间");
		Response response = operate.create(data, user.get("user_id").toString() );
		return response;
	}
}
