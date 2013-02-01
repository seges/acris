package org.springframework.security.userdetails;

import org.springframework.security.GrantedAuthority;

import java.io.Serializable;

public interface UserDetails extends Serializable {

	GrantedAuthority[] getAuthorities();

    String getPassword();

    String getUsername();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();
}