package com.kh.myShop.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.myShop.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@RestController
public class EmployeeController {
	
	@Value("${file.upload-dir}")
	private String uploadDir;

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
	
	//직원 등록
	@PostMapping("/employee/saveEmployee")
	public Map<String, Object> saveEmployee(@RequestParam("info") String info,
											@RequestParam("detail") String detail,
											@RequestParam(value = "img", required = false) MultipartFile img) {
	return employeeService.saveEmployee(info, detail, img);
	}
	
	//직원 상세 조회
	@GetMapping("/getEmployeeInfo")
	public Map<String, Object> getEmployeeInfo(@RequestParam String id) throws Exception{
		return employeeService.getEmployeeInfo(id);
	}
	
	//직원 사진 반환
	@GetMapping("/getEmployeeImg")
	public ResponseEntity<byte[]> getEmployeeImg(@RequestParam("fileName") String fileName) throws IOException {
		Path fileStorageLocation = Paths.get(uploadDir);
		Path filePath = fileStorageLocation.resolve(fileName).normalize();
		Resource resource = new UrlResource(filePath.toUri());
		
		if (resource.exists() && resource.isReadable()) {
			byte[] imageBytes = resource.getInputStream().readAllBytes();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			headers.setContentLength(imageBytes.length);
			headers.setContentDispositionFormData("filename", fileName);
			
			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//직원 정보 수정
	@PostMapping("/employee/updateEmployee")
	public Map<String, Object> updateEmployee(@RequestParam("info") String info,
											@RequestParam("detail") String detail,
											@RequestParam(value = "img", required = false) MultipartFile img) {
		return employeeService.updateEmployee(info, detail, img);
	}
	
	//주민번호 뒷자리 확인
	@PostMapping("/common/getJuminNum")
	public Boolean getJuminNum(@RequestBody Map<String, Object> passwordMap, HttpSession session) {
		return employeeService.getJuminNum(passwordMap, session);
	}
	
	//직원 회원 가입
	@PostMapping("/signup")
	public Map<String,Object> signup(@RequestParam String email,
									 @RequestParam String password,
									 @RequestParam String shop_id,
									 @RequestParam String id) {
		return employeeService.signup(email, password, shop_id, id);
	}
	
	//정보 조회 by 직원
	@GetMapping("/getEmployeeInfoByEmployee")
	public Map<String, Object> getEmployeeInfoByEmployee() throws Exception{
		return employeeService.getEmployeeInfoByEmployee();
	}
	
	//직원 대시보드 정보
	@GetMapping("/getDashBoardInfoByEmployee")
	public Map<String, Object> getDashBoardInfoByEmployee() throws Exception{
		return employeeService.getDashBoardInfoByEmployee();
	}
	
	//출퇴근 기록 저장
	@PostMapping("/employee/saveCommute")
	public Map<String,Object> saveCommute(@RequestBody Map<String, Object> commuteMap) {
		return employeeService.saveCommute(commuteMap);
	}
	
	@GetMapping("/getCommuteList")
	public List<Map<String, Object>> getCommuteList(){
		return employeeService.getCommuteList();
	}
	
	@GetMapping("/getUserCommuteList")
	public List<Map<String, Object>> getUserCommuteList(@RequestParam String year, @RequestParam String month) {
		return employeeService.getUserCommuteList(year, month);
	}
	
	@GetMapping("/getNoticeList")
	public Map<String, Object> getNoticeList(@RequestParam Integer pageNumber) {
		return employeeService.getNoticeList(pageNumber);
	}
	
	@GetMapping("/viewNoticeDesc")
	public Map<String, Object> viewNoticeDesc(@RequestParam String id){
		return employeeService.viewNoticeDesc(id);
	}
}