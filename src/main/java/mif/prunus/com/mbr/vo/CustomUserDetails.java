package mif.prunus.com.mbr.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mif.prunus.com.cmm.Role;

@Getter
@NoArgsConstructor(force = true)
@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails, CredentialsContainer {

	private Long mbrSeq;
	private String email;
	private String password;
	private String mbrId;
	private String mbrNm;
	private String username;

	private Role role;
	private Role roles;

	// == 생성자 Builder ==//
	/*
	 * @Builder public CustomUserDetails(String email, String password, String username, Role roles) { this.email = email; this.password = password; this.username = username; this.roles = roles; }
	 */
	@Builder
	public CustomUserDetails(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;

	}

	public String getRoleKey() {
		return this.role.getKey();
	}

	// == update ==//
	public void update(String password, String username) {
		this.password = password;
		this.username = username;
	}

	@Override
	public void eraseCredentials() {// 인증 후 초기화
		this.password = null;
	}

	// ========== CustomUserDetails implements ==========//
	/**
	 * Token을 고유한 Email 값으로 생성합니다
	 * 
	 * @return email;
	 */
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.roles.name()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}