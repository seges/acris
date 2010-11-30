package sk.seges.acris.security.shared.spring.user_management.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.GrantedAuthority;

import sk.seges.acris.security.shared.spring.authority.GrantedAuthorityImpl;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringAuthoritiesSupport implements Serializable {

	private static final long serialVersionUID = -274900627165199081L;
	private UserData<?> user;

	public SpringAuthoritiesSupport() {
	}

	public void setUser(UserData<?> user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public <E> UserData<E> getUser() {
		return (UserData<E>) user;
	}

	public SpringAuthoritiesSupport(UserData<?> user) {
		this.user = user;
	}

	public GrantedAuthority[] getAuthorities() {
		if (user.getUserAuthorities() == null) {
			return new GrantedAuthority[0];
		}

		GrantedAuthority[] grantedAuthorities = new GrantedAuthority[user.getUserAuthorities().size()];

		int i = 0;
		for (String authority : user.getUserAuthorities()) {
			GrantedAuthorityImpl lazyGrantedAuthority = new GrantedAuthorityImpl();
			lazyGrantedAuthority.setAuthority(authority);
			grantedAuthorities[i++] = lazyGrantedAuthority;
		}

		return grantedAuthorities;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		List<String> result = new ArrayList<String>();

		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				result.add(authority.getAuthority());
			}
		}

		user.setUserAuthorities(result);
	}
}