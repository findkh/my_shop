package com.kh.myShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myShop.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	//목록 조회
	@GetMapping("/getEmployeeList")
	public List<Map<String, Object>> getEmployeeList() {
		return employeeService.getEmployeeList();
	}
	
	//직원 검색 조회
	@GetMapping("/findEmployee")
	public List<Map<String, Object>> findEmployee(@RequestParam String searchKeyword) {
		return employeeService.findEmployee(searchKeyword);
	}
}