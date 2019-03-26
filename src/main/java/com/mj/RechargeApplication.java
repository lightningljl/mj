package com.mj;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mybatis.entity.Recharge;
import com.mybatis.service.RechargeService;
import com.tools.Response;



@Controller
public class RechargeApplication {
	
	@Resource
    private RechargeService rechargeService;
	/**
	 * inquire recharge detail
	 */
	@RequestMapping(value="/recharge/inquire", produces = MediaType.APPLICATION_JSON_VALUE ,method= RequestMethod.GET)
	public Response inquire() {
		Response response = new Response(0, "注册失败");
		Recharge detail = rechargeService.inquire(1);
		System.out.println(detail.getAmount());
		return response;
	}
}
