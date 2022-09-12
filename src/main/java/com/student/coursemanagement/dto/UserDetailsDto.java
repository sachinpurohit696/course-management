package com.student.coursemanagement.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.student.coursemanagement.model.User;

public class UserDetailsDto implements UserDetails {

	private static final long serialVersionUID = 1L;

	  private String username;
	  
	  @JsonIgnore
	  private String password;

	  private Collection<? extends GrantedAuthority> authorities;

	  public UserDetailsDto(Long id, String username, String password,
	      Collection<? extends GrantedAuthority> authorities) {
	    this.username = username;
	    this.password = password;
	    this.authorities = authorities;
	  }

	  public static UserDetailsDto build(User user) {
	    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));

	    return new UserDetailsDto(
	        user.getUserId(), 
	        user.getUserName(), 
	        user.getPassword(), 
	        authorities);
	  }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
