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
	
	Map<String,Object> getEmployeeInfo(Map<String,Object> param);
	
	Map<String,Object> getEmployeeImg(Map<String,Object> param);
	
	void updateUser(Map<String, Object> param);
	
	void updateInfo(Map<String,Object> param);
	
	void updateDetailInfo(Map<String,Object> param);
	
	Map<String,Object> findEmployeeImg(Map<String,Object> param);
	
	void deleteEmployeeImg(Map<String, Object> param);
	
	void updateUserByEmployee(Map<String, Object> param);
	
	Map<String,Object> checkUserId(Map<String, Object> param);
	
	Map<String,Object> getQrCode(Map<String, Object> param);
	
	void saveCommute(Map<String, Object> param);
	
	Map<String, Object> checkCommute(Map<String, Object> param);
	
	List<Map<String, Object>> getCommuteList(Map<String, Object> param);
	
	String getUserEmployeeCode(String userId);
	
	List<Map<String, Object>> getUserCommuteList(Map<String, Object> param);
	
	List<Map<String, Object>> getNoticeList(Map<String, Object> param);
	
	int getTotalRecords();
	
	Map<String, Object> getNoticeDesc(String id);
	
	void saveNotice(Map<String,Object> param);
	
	void updateNotice(Map<String,Object> param);
	
	void deleteNotice(Map<String,Object> param);
	
	void incrementViewCount(String id);
	
	List<Map<String, Object>> getDashBoardNoticeList(Map<String,Object> param);
}