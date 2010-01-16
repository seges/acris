package sk.seges.acris.security.rpc.domain;

public enum Permission {
	EMPTY(0 << 0), VIEW(1 << 0), EDIT(1 << 1), CREATE(1 << 2), DELETE(1 << 3);

	public static final String VIEW_PERMISSION = "VIEW";
	public static final String EDIT_PERMISSION = "EDIT";
	public static final String CREATE_PERMISSION = "CREATE";
	public static final String DELETE_PERMISSION = "DELETE";
	
	private int mask;
	
	Permission(int permissionMask) {
		this.mask = permissionMask;
	}
	
	public int getMask() {
		return mask;
	}
}
