package com.kh.myShop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myShop.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/getMemberList")
	public List<Map<String, Object>> getMemberList(@RequestParam("shop_id") String shopId) {
		return memberService.getMemberList(shopId);
	}
}
