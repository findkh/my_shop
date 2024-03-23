package com.kh.myShop.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.myShop.login.LoginEntity;
import com.kh.myShop.mapper.LoginMapper;

@Service
public class LoginService implements UserDetailsService{

	@Autowired
	private LoginMapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	private PasswordEncoder passwordEncoder;

	public LoginService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		logger.info("LoginService - loadUserByUsername");
		logger.info("login id: {}", user_id);

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("user_id", user_id);

		LoginEntity loginEntity = mapper.getUserInfo(param);
		logger.info("loginEntity: {}", loginEntity);

		if (loginEntity == null ) {
			throw new UsernameNotFoundException("User not authorized.");
		}

		return loginEntity;
	}

	// 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호를 비교하는 메서드
	public boolean authenticate(String username, String password) {
		logger.info("authenticate 실행");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("user_id", username);

		LoginEntity loginEntity = mapper.getUserInfo(param);
		logger.info("loginEntity: {} 실행");
		
		if (loginEntity == null ) {
			return false;
		}

		// DB에 저장된 암호화된 비밀번호
		String encodedPassword = loginEntity.getPassword();

		// 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호를 비교
		return passwordEncoder.matches(password, encodedPassword);
	}
}