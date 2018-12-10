package com.mj;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayApplication {
	
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
}
