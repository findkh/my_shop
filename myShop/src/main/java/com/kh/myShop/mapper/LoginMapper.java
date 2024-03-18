package com.kh.myShop.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.myShop.login.LoginEntity;

@Mapper
public interface LoginMapper {
	LoginEntity getUserInfo(Map<String,Object> param);
}