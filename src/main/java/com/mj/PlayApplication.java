package com.mj;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayApplication {
	@GetMapping(value="/play/index")
	public String index() {
		return "page/play/index";
	}
}
