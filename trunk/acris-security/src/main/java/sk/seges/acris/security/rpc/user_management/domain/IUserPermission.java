package sk.seges.acris.security.rpc.user_management.domain;

import java.io.Serializable;

public interface IUserPermission extends Serializable {
	
	public static final String NONE = "";
	
	public String name();
}
