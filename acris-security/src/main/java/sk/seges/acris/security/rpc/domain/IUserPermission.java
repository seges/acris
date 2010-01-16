package sk.seges.acris.security.rpc.domain;

import java.io.Serializable;

public interface IUserPermission extends Serializable {
	
	public static final String NONE = "";
	
	public String name();
}
