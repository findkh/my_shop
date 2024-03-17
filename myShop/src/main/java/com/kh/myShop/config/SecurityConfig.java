package com.kh.myShop.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.kh.myShop.login.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private CustomUserDetailsService loginService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); //정적자원처리
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf((csrfConfig) ->{
				csrfConfig.disable();
//				csrfConfig.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
			}
			) 
			.headers((headerConfig) ->
				headerConfig.frameOptions(frameOptionsConfig ->
					frameOptionsConfig.disable()
				)
			)
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					.requestMatchers("/login/**", "/statisc/assets/**").permitAll()
//					.requestMatchers("/login/**", "/static/assets/**", "*.css", "*.js").permitAll()
					.anyRequest().authenticated()
			)
//    			.exceptionHandling((exceptionConfig) ->
//    					exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
//    			) 
			.formLogin((formLogin) ->
				formLogin
					.loginPage("/login") 
					.usernameParameter("user_id") 
					.passwordParameter("password") 
					.loginProcessingUrl("/login-proc") 
					.defaultSuccessUrl("/", true)
			)
			.logout((logoutConfig) ->
				logoutConfig.logoutSuccessUrl("/login")
			)
			.userDetailsService(loginService);

		return http.build();
	}
}