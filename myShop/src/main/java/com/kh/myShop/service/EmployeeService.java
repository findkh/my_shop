package com.kh.myShop.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.myShop.login.LoginEntity;
import com.kh.myShop.mapper.EmployeeMapper;
import com.kh.myShop.util.AES256;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
	@Transactional
	public Map<String, Object> saveEmployee(@RequestParam("info") String info,
											@RequestParam("detail") String detail,
											@RequestParam("img") MultipartFile img
											) {
		logger.info("saveEmployee 호출");
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
		
		infoMap.put("id", userMap.get("id").toString());
		detailMap.put("id", userMap.get("id").toString());
		
		employeeMapper.saveInfo(infoMap);
		employeeMapper.saveDetailInfo(detailMap);
		
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
				imgMap.put("id", userMap.get("id").toString());
				String filePath = uploadDir + File.separator + fileName;
				
				// 파일 객체 생성
				File dest = new File(filePath);
				// 이미지 파일을 생성한 파일 객체로 저장
				img.transferTo(dest);
				employeeMapper.saveImgInfo(imgMap);
				
			} catch (IOException e) {
				e.printStackTrace();
				result.put("msg", "Error occurred while saving image");
				return result;
			}
		}
		
		result.put("msg", msg);
		result.put("id", userMap.get("id").toString());
		logger.info("saveEmployee 종료");
		return result;
	}
	
	// 직원 코드 생성 메서드
	public static String generateEmployeeCode() {
		logger.info("generateEmployeeCode 호출");
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
		logger.info("generateEmployeeCode 종료: {}", employeeCode);
		return employeeCode;
	}

	//직원 상세 정보 조회
	public Map<String, Object> getEmployeeInfo(String id) throws Exception {
		logger.info("getEmployeeInfo Service 호출: {}", id);
		Map<String, Object> returnData = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		
		Map<String, Object> result = employeeMapper.getEmployeeInfo(param);
		
		if(result != null) {
			logger.info("result: {}", result);
			AES256 dec = new AES256();
			String decryptedJumin1 = dec.decryptAES256(result.get("jumin_num").toString()).split("-")[0];
			String decryptedJumin2 = dec.decryptAES256(result.get("jumin_num").toString()).split("-")[1];
			
			result.put("phone_number", dec.decryptAES256(result.get("phone_number").toString()));
			result.put("jumin_num1", decryptedJumin1);
			result.put("jumin_num2", decryptedJumin2);
			result.remove("jumin_num");
			Map<String, Object> img = employeeMapper.getEmployeeImg(result);
			
			if(img != null) {
				logger.info("직원 사진 존재");
				result.put("employeeImg", img.get("img_name").toString());
			}
			
			result.put("joinUrl", "http://localhost:8080/login?shop_id="+result.get("_shopId").toString()+"&id="+result.get("id").toString());
			returnData.put("result", result);
		} else {
			returnData.put("result", "invalid");
		}
		
		logger.info("getEmployeeInfo Service 종료");
		return returnData;
	}
	
	//직원 정보 수정
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Object> updateEmployee(@RequestParam("info") String info,
											@RequestParam("detail") String detail,
											@RequestParam("img") MultipartFile img
											) {
		logger.info("updateEmployee 호출");
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
		userMap.put("user_status", infoMap.get("user_status").toString());
		
		employeeMapper.updateUser(userMap);
		employeeMapper.updateInfo(infoMap);
		employeeMapper.updateDetailInfo(detailMap);
		
		//이미지 저장
		if (img != null) {
			logger.info("이미지 존재");
			try {
				// 저장된 이미지 조회
				Map<String, Object> imgResultMap = employeeMapper.findEmployeeImg(infoMap);
				
				if (imgResultMap != null) {
					String imgName = imgResultMap.get("img_name").toString();
					
					// 데이터베이스에서 이미지 정보 삭제
					employeeMapper.deleteEmployeeImg(imgResultMap);
					
					// 이미지 파일의 경로를 생성
					File imgFile = new File(uploadDir, imgName);
					
					// 데이터베이스 삭제가 성공적으로 이루어진 후 파일 삭제
					if (imgFile.exists() && imgFile.isFile()) {
						if (imgFile.delete()) {
							logger.info("이미지 파일 삭제 성공");
						} else {
							logger.error("이미지 파일 삭제 실패: {}", imgFile);
						}
					} else {
						logger.info("삭제할 파일이 존재하지 않음");
					}
				}
				
				//새 이미지 파일 저장 로직
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				
				String fileName = UUID.randomUUID().toString() + "_" + img.getOriginalFilename();
				Map<String, Object> imgMap = new HashMap<>();
				imgMap.put("originFileName", img.getOriginalFilename());
				imgMap.put("encFileName", fileName);
				imgMap.put("id", infoMap.get("id").toString());
				String filePath = uploadDir + File.separator + fileName;
				
				File dest = new File(filePath);
				img.transferTo(dest);
				employeeMapper.saveImgInfo(imgMap);
				
			} catch (IOException e) {
				e.printStackTrace();
				result.put("msg", "Error occurred while saving image");
				return result;
			}
		}
		
		result.put("msg", msg);
		result.put("id", infoMap.get("id").toString());
		logger.info("updateEmployee 종료");
		return result;
	}
	
	//비밀번호 확인 로직
	public Boolean getJuminNum(Map<String, Object> passwordMap, HttpSession session){
		String pwd = passwordMap.get("password").toString();
		PasswordEncoder pwdEnc = new BCryptPasswordEncoder();
		return pwdEnc.matches(pwd, getCurrentUser().getPassword());
	}
	
	//로그인 유저 정보 얻는 메서드
	public UserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() &&
			!(authentication.getPrincipal() instanceof String)) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails;
		}
		return null;
	}
	
	//회원 가입
	@Transactional
	public Map<String, Object> signup(@RequestParam String email,
									  @RequestParam String password,
									  @RequestParam String shop_id,
									  @RequestParam String id) {

		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		
		if (email.isEmpty() || password.isEmpty() || shop_id.isEmpty() || id.isEmpty()) {
			msg = "필수 필드가 누락되었습니다.";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		
		param.put("email", email);
		param.put("shop_id", shop_id);
		param.put("id", id);
		param.put("encPwd", enc.encode(password));
		
		Map<String,Object> check = employeeMapper.checkUserId(param);

		if(Integer.parseInt(check.get("count").toString()) > 0) {
			msg = "가입된 이메일 입니다.";
		} else {
			employeeMapper.updateUserByEmployee(param);
		}
		
		
		result.put("msg", msg);
		
		return result;
	}
	
	//직원이 자기 정보 조회
	public Map<String, Object> getEmployeeInfoByEmployee() throws Exception {
		String id = "";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof LoginEntity) {
			LoginEntity loginEntity = (LoginEntity) authentication.getPrincipal();
			id = loginEntity.getId();
			System.out.println("ID 값: " + id);
		} else {
			System.out.println("인증 정보가 올바르지 않습니다.");
		}
		
		logger.info("getEmployeeInfoByEmployee Service 호출 로그인 id: {}", id);
		
		Map<String, Object> returnData = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		
		Map<String, Object> result = employeeMapper.getEmployeeInfo(param);
		
		if(result != null) {
			logger.info("result: {}", result);
			AES256 dec = new AES256();
			String decryptedJumin1 = dec.decryptAES256(result.get("jumin_num").toString()).split("-")[0];
			String decryptedJumin2 = dec.decryptAES256(result.get("jumin_num").toString()).split("-")[1];
			
			result.put("phone_number", dec.decryptAES256(result.get("phone_number").toString()));
			result.put("jumin_num1", decryptedJumin1);
			result.put("jumin_num2", decryptedJumin2);
			result.remove("jumin_num");
			Map<String, Object> img = employeeMapper.getEmployeeImg(result);
			
			if(img != null) {
				logger.info("직원 사진 존재");
				result.put("employeeImg", img.get("img_name").toString());
			}
			
			returnData.put("result", result);
		} else {
			returnData.put("result", "invalid");
		}
		
		logger.info("getEmployeeInfo Service 종료");
		return returnData;
	}
	
	//대시보드 정보 조회
	public Map<String, Object> getDashBoardInfo() throws Exception {
		Map<String, Object> result = new HashMap<>();
		LoginEntity userInfo = (LoginEntity) getCurrentUser();
		String userRole = userInfo.getAuthorities().toString();
		String id = userInfo.getId().toString();
		String userName = userInfo.getUser_name().toString();
		Map<String,Object> param = new HashMap<>();
		
		result.put("userName", userName);
		result.put("noticeList",employeeMapper.getDashBoardNoticeList(param));
		
		if(userRole.equals("[ROLE_ADMIN]")) {
			result.put("commuteList", getCommuteList());
		} else if(userRole.equals("[ROLE_USER]")){
			result.put("code", getDashBoardInfoByEmployee(id));
		}
		
		return result;
	}
	
	//근로자 대시보드 정보 조회
	public Map<String, Object> getDashBoardInfoByEmployee(String id) throws Exception {
		logger.info("getDashBoardInfoByEmployee Service 호출 로그인 id: {}", id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return employeeMapper.getQrCode(param);
	}
	
	//촐퇴근 기록 저장
	@Transactional
	public Map<String,Object> saveCommute(Map<String, Object> commuteMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		logger.info("saveCommute Service 호출");
		
		try {
			//중복 체크 로직
			LocalDate today = LocalDate.now();
			String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			commuteMap.put("today", formattedDate);
			Map<String, Object> checkResult = employeeMapper.checkCommute(commuteMap);
			if(Integer.parseInt(checkResult.get("count").toString()) == 0) {
				employeeMapper.saveCommute(commuteMap);
			} else {
				msg = "none";
			}
			
		} catch(Exception e){
			e.printStackTrace();
			msg = "fail";
		}
		
		result.put("msg", msg);
		
		logger.info("saveCommute Service 종료");
		return result;
	}
	
	//출퇴근 목록 조회
	public List<Map<String, Object>> getCommuteList() {
		logger.info("getCommuteList Service 호출");
		
		// 오늘 날짜를 가져와서 YYYY-MM-DD 형식으로 변환
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		// 파라미터 맵에 오늘 날짜를 넣어줌
		Map<String,Object> param = new HashMap<>();
		param.put("today", formattedDate);
		
		// 맵을 사용하여 쿼리 실행
		List<Map<String, Object>> result = employeeMapper.getCommuteList(param);
		
		logger.info("result: {}", result);
		return result;
	}
	
	//출근자 리스트 조회
	public List<Map<String, Object>> getUserCommuteList(String year, String month) {
		logger.info("getUserCommuteList Service 호출 year: {} month: {}", year, month);
		Map<String, Object> param = new HashMap<>();
		String userId = getCurrentUser().getUsername().toString();
		
		String employeeCode = employeeMapper.getUserEmployeeCode(userId);
		
		param.put("year-month", year+"-"+month);
		param.put("employeeCode", employeeCode);
		
		List<Map<String, Object>> result = employeeMapper.getUserCommuteList(param);
		logger.info("result: {}", result);
		return result;
	}
	
	//공지사항 목록 조회
	public Map<String, Object> getNoticeList(Integer pageNumber) {
		logger.info("getNoticeList Service 호출 pageNumber: {}", pageNumber);
		
		// 페이지 당 데이터 수
		int pageSize = 10;
		
		// 총 데이터 수 조회
		int totalRecords = employeeMapper.getTotalRecords();
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		
		// pageNumber가 1보다 작으면 1로 설정, totalPages보다 크면 totalPages로 설정
		if (pageNumber < 1) {
			pageNumber = 1;
		} else if (pageNumber > totalPages) {
			pageNumber = totalPages;
		}
		
		int offset = (pageNumber - 1) * pageSize;
		
		Map<String, Object> param = new HashMap<>();
		param.put("limit", pageSize);
		param.put("offset", offset);
		
		List<Map<String, Object>> noticeList = employeeMapper.getNoticeList(param);
		Map<String, Object> result = new HashMap<>();
		result.put("noticeList", noticeList);
		result.put("totalPages", totalPages);
		
		return result;
	}
	
	//공지사항 상세보기
	public Map<String, Object> getNoticeDesc(String id, HttpServletRequest request, HttpServletResponse response) {
		logger.info("getNoticeDesc Service 호출");
		Map<String, Object> result = employeeMapper.getNoticeDesc(id);
		if(result != null) {
			String createdId = result.get("created_id").toString();
			String currentUser = getCurrentUser().getUsername().toString();
			if(createdId.equals(currentUser)) {
				result.put("flag", "true");
			} else {
				result.put("flag", "false");
				handleViewCount(id, request, response);
			}
		}
		logger.info("getNoticeDesc Service 종료");
		return result;
	}
	
	//조회수를 증가시키고, 조회 기록을 쿠키에 저장하여 중복 조회를 방지
	private void handleViewCount(String id, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		boolean isViewed = false;
		String cookieName = "notice_viewed_" + id;
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookieName.equals(cookie.getName())) {
					isViewed = true;
					break;
				}
			}
		}
		if(!isViewed) {
			employeeMapper.incrementViewCount(id);
			Cookie newCookie = new Cookie(cookieName, "true");
			newCookie.setMaxAge(getSecondsUntilMidnight());
			newCookie.setPath("/");
			response.addCookie(newCookie);
		}
	}
	
	//자정(한국 시간 기준)까지 남은 초(seconds)를 계산하여 쿠키의 만료 시간을 설정
	private int getSecondsUntilMidnight() {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
		return (int) Duration.between(now, midnight).getSeconds();
	}
	
	//공지사항 저장
	public Map<String,Object> saveNotice(Map<String, Object> noticeMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		logger.info("saveNotice Service 호출");
		try {
			employeeMapper.saveNotice(noticeMap);
			result.put("id", noticeMap.get("id"));
		} catch(Exception e){
			e.printStackTrace();
			msg = "fail";
		}
		
		result.put("msg", msg);
		
		logger.info("saveNotice Service 종료");
		return result;
	}
	
	//공지사항 수정
	public Map<String,Object> updateNotice(Map<String, Object> noticeMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		logger.info("updateNotice Service 호출");
		try {
			employeeMapper.updateNotice(noticeMap);
		} catch(Exception e){
			e.printStackTrace();
			msg = "fail";
		}
		
		result.put("msg", msg);
		
		logger.info("updateNotice Service 종료");
		return result;
	}
	
	//공지사항 삭제
	public Map<String,Object> deleteNotice(Map<String, Object> noticeMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		String msg = "success";
		logger.info("deleteNotice Service 호출");
		try {
			employeeMapper.deleteNotice(noticeMap);
		} catch(Exception e){
			e.printStackTrace();
			msg = "fail";
		}
		
		result.put("msg", msg);
		
		logger.info("deleteNotice Service 종료");
		return result;
	}
}
