package sk.seges.acris.security.shared.util;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.SecurityConstants;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class UserDataUtils {
	
	public static boolean hasAuthority(UserData user, String authority) {
		if (user != null) {
			List<String> authorities = user.getUserAuthorities();
			if (authorities != null) {
				return authorities.contains(authority) || authorities.contains(SecurityConstants.AUTH_PREFIX + authority);
			}
		}
		return false;
	}
}
