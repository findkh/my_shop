package com.kh.myShop.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kh.myShop.service.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private LoginService loginService;

	// BCryptPasswordEncoder 빈을 생성하는 메서드
	@Bean
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 정적 자원에 대한 요청을 무시하는 웹 보안 커스터마이저 빈을 생성하는 메서드
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	// 인증 실패 시 처리를 담당하는 핸들러를 생성하는 메서드
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/login?error"); // 실패 시 /login 페이지로 리다이렉트하며 실패 메시지 전달
	}

	// 보안 필터 체인을 설정하는 메서드
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			//CSRF 보호 설정
			.csrf((csrfConfig) ->
				csrfConfig.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			)
//		.csrf(c -> c.disable())
			// 헤더 설정 (X-Frame-Options 비활성화)
			.headers((headerConfig) ->
				headerConfig.frameOptions(frameOptionsConfig ->
					frameOptionsConfig.disable()
				)
			)
			// HTTP 요청에 대한 인가 설정
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					.requestMatchers("/login/**", "/assets/**", "/signup/**").permitAll() // 로그인 및 정적 자원에 대한 접근 허용
					.requestMatchers("/employee/**").hasRole("ADMIN")
					.requestMatchers("/common/**").hasAnyRole("ADMIN", "USER")
					.anyRequest().authenticated() // 그 외 요청에 대해서는 인증 필요
			)
			// 폼 로그인 설정
			.formLogin((formLogin) ->
				formLogin
					.loginPage("/login") // 로그인 페이지 설정
					.usernameParameter("user_id") 
					.passwordParameter("password") 
					.loginProcessingUrl("/login-proc") 
					.defaultSuccessUrl("/", true) // 로그인 성공 시 기본 URL 설정
					.failureHandler(authenticationFailureHandler()) // 인증 실패 시 핸들러 설정
			)
			// 로그아웃 설정
			.logout((logoutConfig) ->
				logoutConfig
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 요청 매처 설정
					.logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트할 URL 설정
					.invalidateHttpSession(true) // HTTP 세션 무효화 설정
			)
			.userDetailsService(loginService);

		return http.build();
	}

}