package sk.seges.acris.security.rpc.user_management.domain;

/**
 * <pre>
 * Permission is the base stone in security. Each user authority is composed
 * from an assigned Permission and a grant. For example: ROLE_USER_MAINTENANCE_VIEW consists of the VIEW
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

	/**
	 * @deprecated Use VIEW_SUFFIX instead and be aware of leading underscore.
	 */
	@Deprecated
	public static final String VIEW_PERMISSION = "PERMISSION_VIEW_NAME";
	/**
	 * @deprecated Use EDIT_SUFFIX instead and be aware of leading underscore.
	 */
	@Deprecated	
	public static final String EDIT_PERMISSION = "PERMISSION_EDIT_NAME";
	/**
	 * @deprecated Use CREATE_SUFFIX instead and be aware of leading underscore.
	 */
	@Deprecated	
	public static final String CREATE_PERMISSION = "CREATE";
	/**
	 * @deprecated Use DELETE_SUFFIX instead and be aware of leading underscore.
	 */
	@Deprecated
	public static final String DELETE_PERMISSION = "DELETE";

	public static final String VIEW_SUFFIX = "_VIEW";
	public static final String EDIT_SUFFIX = "_EDIT";
	public static final String CREATE_SUFFIX = "_CREATE";
	public static final String DELETE_SUFFIX = "_DELETE";
	
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