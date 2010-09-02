package sk.seges.acris.security.rebind;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.shared.user_management.domain.Permission;

import com.google.gwt.core.ext.typeinfo.HasAnnotations;

public class SecuredAnnotationProcessor implements ISecuredAnnotationProcessor {
	
	/**
	 * Method for transforming secured annotation into the array of authorities
	 * represented as String
	 * 
	 * @param classType
	 * @return
	 */
	public String[] getAuthoritiesForType(HasAnnotations annotationType) {
		Secured securedAnnotation = annotationType.getAnnotation(Secured.class);

		if (securedAnnotation == null) {
			return null;
		}

		Permission permission = securedAnnotation.permission();
		
		if (permission != null && !Permission.EMPTY.equals(permission)) {
			String[] authorities = securedAnnotation.value();
			
			for (int i = 0; i < authorities.length; i++) {
				if (authorities[i].length() > 0) {
					authorities[i] = authorities[i] + "_" + permission.name();
				}
			}
			return authorities;
		}
		return securedAnnotation.value();
	}

	public List<String> getListAuthoritiesForType(HasAnnotations annotationType) {
		String[] authorities = getAuthoritiesForType(annotationType);

		if (authorities == null) {
			return null;
		}

		List<String> roles = new ArrayList<String>();
		for (String permission : authorities) {
			if (!permission.equals("")) {
				roles.add(permission);
			}
		}

		return roles;
	}

	@Override
	public boolean isAuthorityPermission(HasAnnotations annotationType) {
		Secured securedAnnotation = annotationType.getAnnotation(Secured.class);

		if (securedAnnotation == null) {
			return false;
		}

		Permission permission = securedAnnotation.permission();
		
		return (permission != null && !Permission.EMPTY.equals(permission));
	}
}
