package com.kh.myShop.login;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class LoginEntity implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String id;
	private String user_id;
	private String password;
	private String user_name;
	private String user_role;
	private String shop_id;
	private String user_status;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.user_role));
	}

	@Override
	public String getUsername() {
		return this.user_id;
	}

	// 사용자의 계정이 만료되지 않았는지 여부를 반환 (기본값은 true)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 사용자의 계정이 잠겨있지 않은지 여부를 반환 (기본값은 true)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 사용자의 자격 증명(비밀번호)이 만료되지 않았는지 여부를 반환 (기본값은 true)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 사용자가 활성화된 계정인지 여부를 반환 (기본값은 true)
	@Override
	public boolean isEnabled() {
		return true;
	}

	// 매핑되지 않은 필드 추가
	public String getShop_id() {
		return this.shop_id;
	}

	public void setShop_no(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getUser_status(){
		return this.user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
}