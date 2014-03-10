package sk.seges.acris.security.server.spring.authority;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

	private static final long serialVersionUID = -5104192029595887683L;

	private String authority;

	public GrantedAuthorityImpl() {
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return obj.equals(this.authority);
		}

		if (obj instanceof GrantedAuthority) {
			GrantedAuthority attr = (GrantedAuthority) obj;
			return this.authority.equals(attr.getAuthority());
		}

		return false;
	}

	public String getAuthority() {
		return this.authority;
	}

	public int hashCode() {
		return this.authority.hashCode();
	}

	public String toString() {
		return this.authority;
	}

	public int compareTo(Object o) {
		if (o != null && o instanceof GrantedAuthority) {
			String rhsRole = ((GrantedAuthority) o).getAuthority();

			if (rhsRole == null) {
				return -1;
			}

			return authority.compareTo(rhsRole);
		}
		return -1;
	}
}