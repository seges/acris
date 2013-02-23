package sk.seges.acris.pap.security.provider;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.shared.user_management.domain.Permission;

public class StringAuthoritiesProvider implements SecuredAuthoritiesProvider {

	/**
	 * Method for transforming secured annotation into the array of authorities represented as String
	 * 
	 * @param classType
	 * @return
	 */
	public String[] getAuthoritiesForType(Element element) {
		Secured securedAnnotation = element.getAnnotation(Secured.class);

		if (securedAnnotation == null) {
			return null;
		}

		Permission permission = securedAnnotation.permission();

		if (permission != null && !Permission.EMPTY.equals(permission)) {
			String[] authorities = securedAnnotation.value();

			for (int i = 0; i < authorities.length; i++) {
				if (authorities[i].length() > 0) {
					authorities[i] = authorities[i];
				}
			}
			return authorities;
		}
		return securedAnnotation.value();
	}

	public List<String> getListAuthoritiesForType(Element element) {
		String[] authorities = getAuthoritiesForType(element);

		if (authorities == null) {
			return null;
		}

		List<String> roles = new ArrayList<String>();
		
		for (String permission : authorities) {
			if (!permission.isEmpty()) {
				roles.add(permission);
			}
		}

		return roles;
	}
}