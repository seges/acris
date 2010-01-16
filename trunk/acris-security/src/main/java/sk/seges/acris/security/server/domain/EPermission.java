package sk.seges.acris.security.server.domain;


public enum EPermission {
	USER_PANEL("gui.temp.SecuredUserPanel"),
	USER_PANEL_FIRSTNAME(USER_PANEL, "firstName"),
	USER_PANEL_DESCRIPTION(USER_PANEL, "description"),
	USER_PANEL_SUBMIT(USER_PANEL, "submit");
	
	private String permission;
	
	EPermission(String permission) {
		this.permission = permission;
	};

	EPermission(EPermission permission, String component) {
		this.permission = permission.getPermission() + "." + component;
	};
	
	public String getPermission() {
		return permission;
	}
}