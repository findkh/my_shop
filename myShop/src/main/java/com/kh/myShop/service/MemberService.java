package com.kh.myShop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.myShop.mapper.MemberMapper;

@Service
public class MemberService {
	@Autowired
	MemberMapper memberMapper;
	
	// 회원 목록을 가져오는 메서드
	public List<Map<String, Object>> getMemberList(String shopId) {
		System.out.println("shopId: "+ shopId);
		List<Map<String, Object>> result = memberMapper.getMemberList(shopId);
		return result;
	}
}
