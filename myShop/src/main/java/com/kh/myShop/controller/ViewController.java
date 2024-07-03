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
	
	@GetMapping("/commute_mng")
	public String commuteMng() {
		return "contents/commute_mng";
	}
	
	@GetMapping("/viewCommute")
	public String viewCommute() {
		return "contents/commute_view";
	}
	
	@GetMapping("/noticeList")
	public String noticeList() {
		return "contents/notice_list";
	}
	
	@GetMapping("/viewNotice")
	public String viewNotice() {
		return "contents/notice_view";
	}
	
	@GetMapping("/addNotice")
	public String addNotice() {
		return "contents/notice_add";
	}
	
	@GetMapping("/editNotice")
	public String editNotice() {
		return "contents/notice_edit";
	}
}
