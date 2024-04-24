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
	
	@GetMapping("/employeeList")
	public String employee() {
		return "contents/employee_list";
	}
	
	@GetMapping("/viewEmployeeInfo")
	public String viewEmployeeInfo() {
		return "contents/employee_view";
	}
	
	@GetMapping("/addEmployeeInfo")
	public String addEmployeeInfo() {
		return "contents/employee_add";
	}
	
	@GetMapping("/employeeInfo")
	public String employeeInfo() {
		return "contents/employee_Info";
	}
}
