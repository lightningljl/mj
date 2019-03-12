package com.mybatis.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mybatis.entity.Recharge;
import com.mybatis.mapper.RechargeMapper;


@Service
public class RechargeServiceImpl implements RechargeService {
    
	@Resource
	private RechargeMapper rechargeMapper;
	
	@Override
	public Recharge inquire(int id) {
		return rechargeMapper.inquire(id);
	}

}
