package com.kh.myShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	@GetMapping("/")
	public String index() {
		return "contents/index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	
	@GetMapping("/employee")
	public String employee() {
		return "contents/employee";
	}
	
	@GetMapping("/viewEmployeeInfo")
	public String viewEmployeeInfo() {
		return "contents/employee_view";
	}
}
