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
	private String user_id; // username 대신 user_id를 사용
    private String password;
    private String user_name;
    private String user_role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.user_role));
    }

    // UserDetails 인터페이스 구현 메서드들

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
}