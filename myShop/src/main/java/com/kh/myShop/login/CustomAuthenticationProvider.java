package com.kh.myShop.login;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kh.myShop.service.LoginService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final LoginService loginService;
	
	public CustomAuthenticationProvider(LoginService loginService, PasswordEncoder passwordEncoder) {
		this.loginService = loginService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		if (loginService.authenticate(username, password)) {
			UserDetails userDetails = loginService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		} else {
			throw new BadCredentialsException("Invalid username or password");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}