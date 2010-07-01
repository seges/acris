package org.springframework.security;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable, Comparable {

    String getAuthority();
}
