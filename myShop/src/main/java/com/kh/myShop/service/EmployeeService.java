package com.kh.myShop.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.myShop.mapper.EmployeeMapper;
import com.kh.myShop.util.AES256;

@Service
public class EmployeeService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
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
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveEmployee(@RequestParam("info") String info,
											@RequestParam("detail") String detail,
											@RequestParam("img") MultipartFile img
											) {
		Map<String, Object> result = new HashMap<>();
		String msg = "success";
		
		
		// ObjectMapper를 사용하여 JSON 문자열을 Map으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> infoMap = new HashMap<String,Object>();
		Map<String, Object> detailMap = new HashMap<String,Object>();
		try {
			infoMap = objectMapper.readValue(info, Map.class);
			detailMap = objectMapper.readValue(detail, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "Error occurred while parsing JSON");
			return result;
		}
		
		//직원 번호 생성
		infoMap.put("employeeCode", generateEmployeeCode());
		infoMap.put("status", "active");
		
		//주민번호 암호화
		if(!infoMap.get("jumin_num").toString().equals("-")) {
			AES256 enc = new AES256();
			try {
				infoMap.put("enc_jumin", enc.encryptAES256(infoMap.get("jumin_num").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				result.put("msg", "Error occurred while encoder Jumin Number");
				logger.info("Error occurred while encoder jumin number");
			}
		}
		
		//전화번호 암호화
		if(!infoMap.get("tel").toString().isEmpty()) {
			AES256 enc = new AES256();
			try {
				infoMap.put("enc_tel", enc.encryptAES256(infoMap.get("tel").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				result.put("msg", "Error occurred while encoder Jumin Number");
				logger.info("Error occurred while encoder jumin number");
			}
		}
		
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userName", infoMap.get("name").toString());
		userMap.put("userStatus", "active");
		userMap.put("userRole", "USER");
		
		employeeMapper.saveUser(userMap);
		
		infoMap.put("id", infoMap.get("id").toString());
		detailMap.put("id", infoMap.get("id").toString());
		
		System.out.println(infoMap);
//		employeeMapper.saveInfo(infoMap);
//		logger.info("resultMap, {}", resultMap);
//		employeeMapper.saveDetail(detailMap);
		
		//이미지 저장
		if (img != null) {
			logger.info("이미지 존재");

			try {
				// 파일 경로 생성
				File directory = new File(uploadDir);
				// 디렉토리가 존재하지 않으면 생성
				if (!directory.exists()) {
					directory.mkdirs();
				}
				
				String fileName = UUID.randomUUID().toString() + "_" + img.getOriginalFilename();
				Map<String, Object> imgMap = new HashMap<String, Object>();
				imgMap.put("originFileName", img.getOriginalFilename());
				imgMap.put("encFileName", fileName);
				imgMap.put("id", infoMap.get("id").toString());
				String filePath = uploadDir + File.separator + fileName;
				
				// 파일 객체 생성
				File dest = new File(filePath);
				// 이미지 파일을 생성한 파일 객체로 저장
				img.transferTo(dest);
//				employeeMapper.saveImgInfo(imgMap);
				
			} catch (IOException e) {
				e.printStackTrace();
				result.put("msg", "Error occurred while saving image");
				return result;
			}
		}
		
		result.put("msg", msg);
		return result;
	}
	
	public static String generateEmployeeCode() {
		// 오늘 날짜를 YYYYMMDD 형식으로 가져옴
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String today = dateFormat.format(new Date());
		
		// 랜덤한 숫자와 영어를 섞은 8자리 코드 생성
		String randomChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder employeeCodeBuilder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			employeeCodeBuilder.append(randomChars.charAt(random.nextInt(randomChars.length())));
		}
		
		// 오늘 날짜와 랜덤한 숫자와 영어를 섞은 코드를 합쳐서 직원 코드 생성
		String employeeCode = today + employeeCodeBuilder.toString();
		return employeeCode;
	}
}
