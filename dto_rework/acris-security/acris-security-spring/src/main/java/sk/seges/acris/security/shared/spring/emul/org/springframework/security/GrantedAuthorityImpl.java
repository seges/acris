package org.springframework.security;

import java.io.Serializable;

public class GrantedAuthorityImpl implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;
    private String role;

    public GrantedAuthorityImpl(String role) {
        this.role = role;
    }

    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.role);
        }

        if (obj instanceof GrantedAuthority) {
            GrantedAuthority attr = (GrantedAuthority) obj;

            return this.role.equals(attr.getAuthority());
        }

        return false;
    }

    public String getAuthority() {
        return this.role;
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }

	public int compareTo(Object o) {
		if (o != null && o instanceof GrantedAuthority) {
			String rhsRole = ((GrantedAuthority) o).getAuthority();
			
			if (rhsRole == null) {
				return -1;
			}
			
			return role.compareTo(rhsRole);
		}
		return -1;
	}
}