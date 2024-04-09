package com.kh.myShop.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {
	List<Map<String,Object>> getEmployeeList(Map<String,Object> param);
	
	List<Map<String,Object>> findEmployee(Map<String,Object> param);
	
	void saveUser(Map<String, Object> param);
	
	void saveInfo(Map<String,Object> param);
	
	void saveDetailInfo(Map<String,Object> param);

	void saveImgInfo(Map<String,Object> param);
}