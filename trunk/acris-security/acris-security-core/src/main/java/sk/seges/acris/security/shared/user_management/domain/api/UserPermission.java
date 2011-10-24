package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;

public interface UserPermission extends Serializable {
	
	public static final String NONE = "";
	
	public String name();
}
