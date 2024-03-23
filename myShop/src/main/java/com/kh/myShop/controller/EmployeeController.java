package com.kh.myShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.myShop.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	EmployeeService employService;

	@GetMapping("/getMemberList")
	public List<Map<String, Object>> getMemberList() {
		return employService.getMemberList();
	}
}