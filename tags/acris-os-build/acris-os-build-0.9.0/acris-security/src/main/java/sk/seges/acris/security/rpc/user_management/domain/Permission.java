package sk.seges.acris.security.rpc.user_management.domain;

/**
 * <pre>
 * Permission used as a base stone in security. Each user authority is composed
 * from assigned Permission. For example: USER_MAINTENANCE_VIEW consists of PERMISSION_VIEW_NAME
 * permission.
 * </pre>
 * 
 * <pre>
 * Each permission has a mask representation which is hand-by-hand with ACL domain level security.
 * </pre>
 * 
 * @author fat
 */
public enum Permission {
	EMPTY(0 << 0), VIEW(1 << 0), EDIT(1 << 1), CREATE(1 << 2), DELETE(1 << 3);

	public static final String VIEW_PERMISSION = "PERMISSION_VIEW_NAME";
	public static final String EDIT_PERMISSION = "PERMISSION_EDIT_NAME";
	public static final String CREATE_PERMISSION = "CREATE";
	public static final String DELETE_PERMISSION = "DELETE";

	/**
	 * Bit mask representation of the permission
	 */
	private int mask;

	Permission(int permissionMask) {
		this.mask = permissionMask;
	}

	public int getMask() {
		return mask;
	}
}