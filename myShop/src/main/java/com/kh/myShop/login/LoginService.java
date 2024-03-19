package com.kh.myShop.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.myShop.mapper.LoginMapper;

@Service
public class LoginService implements UserDetailsService{

	@Autowired
	private LoginMapper mapper;

	private PasswordEncoder passwordEncoder;

	public LoginService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		System.out.println("LoginService loadUserByUsername");
		System.out.println("username : " + user_id);

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("user_id", user_id);

		LoginEntity loginEntity = mapper.getUserInfo(param);
		System.out.println("loginEntity : " + loginEntity);

		if (loginEntity == null ) {
			throw new UsernameNotFoundException("User not authorized.");
		}

		return loginEntity;
	}

	// 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호를 비교하는 메서드
	public boolean authenticate(String username, String password) {
		System.out.println("authenticate 호출됨");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("user_id", username);

		LoginEntity loginEntity = mapper.getUserInfo(param);

		if (loginEntity == null ) {
			return false;
		}

		// DB에 저장된 암호화된 비밀번호
		String encodedPassword = loginEntity.getPassword();

		// 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호를 비교
		return passwordEncoder.matches(password, encodedPassword);
	}
}