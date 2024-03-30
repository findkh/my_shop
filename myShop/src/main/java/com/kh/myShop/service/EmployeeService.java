package com.kh.myShop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.myShop.mapper.EmployeeMapper;

@Service
public class EmployeeService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	// 직원 목록을 가져오는 메서드
	public List<Map<String, Object>> getEmployeeList() {
		logger.info("getEmployeeList Service 호출");
		Map<String,Object> param = new HashMap<>();
		List<Map<String, Object>> result = employeeMapper.getEmployeeList(param);
		logger.info("result: {}", result);
		return result;
	}
	
	//검색어에 따라 직원 리스트를 가져오는 메서드
	public List<Map<String, Object>> findEmployee(String searchKeyword) {
		logger.info("findEmployee Service 호출: {}", searchKeyword);
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", searchKeyword);
		List<Map<String, Object>> result = employeeMapper.findEmployee(param);
		logger.info("result: {}", result);
		return result;
	}
	
	// 직원 저장 메서드
	public Map<String, Object> saveEmployee(@RequestParam("info") String info,
			@RequestParam("detail") String detail,
			@RequestParam("img") MultipartFile img) {
		Map<String, Object> result = new HashMap<>();
		String msg = "success";
		
		result.put("msg", msg);
		return result;
	}
}
