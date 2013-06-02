package sk.seges.sesam.security.shared.model.api;

import java.io.Serializable;

public class PermissionData implements Serializable {

	private static final long serialVersionUID = -3233324058166012172L;
	
	private boolean visible;
	private String roleName;

	public PermissionData() {};
	
	public PermissionData(String roleName) {
		this.roleName = roleName;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return this.roleName;
	}
}