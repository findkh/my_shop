package com.kh.myShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/")
	public String index() {
		return "contents/index";
	}
}