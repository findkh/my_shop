package com.kh.myShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	//페이지 mapping
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
}
