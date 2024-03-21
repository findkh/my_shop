package com.kh.myShop.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	List<Map<String,Object>> getMemberList(String shopId);
}