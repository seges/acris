package sk.seges.sesam.security.shared.model.api;

public class PermissionData {

	private boolean visible;
	private String roleName;

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