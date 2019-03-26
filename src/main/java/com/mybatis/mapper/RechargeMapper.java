package com.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import com.mybatis.entity.Recharge;

public interface RechargeMapper {
	Recharge inquire(int id);
}
