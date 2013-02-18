package sk.seges.acris.security.shared.util;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.SecurityConstants;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SecurityUtils {

	private static boolean hasAuthority(String permissionPrefix, GenericUserDTO user, String... authorities) {
		boolean result = authorities == null || authorities.length == 0;
		for (String authority: authorities) {
			result = result || SecurityUtils.hasAuthority(permissionPrefix, user, authority);
			if (result) {
				return result;
			}
		}
		
		return result;
	}
	
	public static void handlePermission(GenericUserDTO user, Composite composite, String ...authorities) {
		handlePermission(user, composite.asWidget(), authorities);
	}
	
	/**
	 * Do not change method definition unless you change it in sk.seges.acris.pap.security.SecurityProcessor!
	 */
	public static void handlePermission(GenericUserDTO user, Widget widget, String[] runtimeAuthorities, String ...staticAuthorities) {
		String[] mergedAuthorities = new String[
		             runtimeAuthorities == null ? 0 : runtimeAuthorities.length + staticAuthorities.length];
		
		int i = 0;
		if (runtimeAuthorities != null) {
			for (String authority: runtimeAuthorities) {
				mergedAuthorities[i++] = authority;
			}
		}
		
		for (String authority: staticAuthorities) {
			mergedAuthorities[i++] = authority;
		}

		handlePermission(user, widget, mergedAuthorities);
	}
	
	public static void handlePermission(GenericUserDTO user, Widget widget, String ...authorities) {

		boolean hasViewPermission = authorities == null || authorities.length == 0;
		
		if (user != null) {
			hasViewPermission = hasAuthority(Permission.VIEW_SUFFIX, user, authorities);
		}

		if (widget != null) {
			if (widget.isVisible() != hasViewPermission) {
				widget.setVisible(hasViewPermission);
			}
		}
		
		if (hasViewPermission && isWidgetEditable(widget)) {
			boolean hasEditPermission = hasAuthority(Permission.EDIT_SUFFIX, user, authorities);
			((HasEnabled)widget).setEnabled(hasEditPermission);
		}
	}

	public static void handleEnabledState(HasEnabled widget, boolean enabled) {
		widget.setEnabled(enabled);
	}

	public static void handleVisibility(UIObject widget, boolean visible) {
		widget.setVisible(visible);
	}

	public static void handleEnabledState(GenericUserDTO user, HasEnabled widget, String... authorities) {
		if (user != null) {
			boolean permission = true;
			
			for (String authority: authorities) {
				permission = permission || hasAuthority(null, user, authority);
			}
			
			widget.setEnabled(permission);
		} else {
			widget.setEnabled(true);
		}
	}

	public static void handleVisibility(GenericUserDTO user, UIObject widget, String... authorities) {
		if (user != null) {
			boolean permission = true;
			
			for (String authority: authorities) {
				permission = permission || hasAuthority(null, user, authority);
			}
						
			widget.setVisible(permission);
		} else {
			widget.setVisible(true);
		}
	}
	
	private static boolean isWidgetEditable(Widget widget) {
		return widget instanceof HasEnabled;
	}
	
	public static boolean hasAuthority(String permissionPrefix, GenericUserDTO user, String authority) {
		if (user != null) {
			List<String> authorities = user.getUserAuthorities();
			if (authorities != null) {
				return authorities.contains(authority) || authorities.contains(SecurityConstants.AUTH_PREFIX + authority) || 
						authorities.contains(SecurityConstants.AUTH_PREFIX + authority + permissionPrefix);
			}
		}
		return false;
	}	
}