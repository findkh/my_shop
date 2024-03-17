package com.kh.myShop.login;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private PasswordEncoder passwordEncoder;
	
	public CustomUserDetailsService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		System.out.println("===================");
		System.out.println(user_id);
		System.out.println(passwordEncoder.encode("1234"));
		UserDetails userDetails = User.builder().username("adminSuper")
				.password(passwordEncoder.encode("1234"))
				.authorities("ROLE_ADMIN")
				.build();
		
		return userDetails;
	}
}
