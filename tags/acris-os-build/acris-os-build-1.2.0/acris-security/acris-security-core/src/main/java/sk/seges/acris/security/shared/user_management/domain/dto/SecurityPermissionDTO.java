package sk.seges.acris.security.shared.user_management.domain.dto;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.sesam.domain.IMutableDomainObject;

public class SecurityPermissionDTO implements HierarchyPermission, IMutableDomainObject<Integer> {

	private static final long serialVersionUID = -8053014688760726531L;

	private Integer id;

	private String permission;

	private String discriminator;

	private String webId;

	private HierarchyPermission parent;

	private List<HierarchyPermission> childPermissions;

	private Boolean hasChildren = false;

	private Integer level = 1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public HierarchyPermission getParent() {
		return parent;
	}

	public void setParent(HierarchyPermission parent) {
		this.parent = parent;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public List<HierarchyPermission> getChildPermissions() {
		return childPermissions;
	}

	public void setChildPermissions(List<HierarchyPermission> childPermissions) {
		if (null != childPermissions && childPermissions.size() > 0) {
			hasChildren = true;
			for (HierarchyPermission childPermission : childPermissions) {
				childPermission.setLevel(getLevel() + 1);
				childPermission.setParent(this);
			}
		}
		this.childPermissions = childPermissions;
	}

	public void addChildPermissions(HierarchyPermission... permissions) {
		if (null == permissions || permissions.length == 0) {
			throw new IllegalArgumentException();
		}
		if (null == childPermissions) {
			childPermissions = new ArrayList<HierarchyPermission>();
			hasChildren = true;
		}
		for (HierarchyPermission permission : permissions) {
			permission.setLevel(getLevel() + 1);
			permission.setParent(this);
			childPermissions.add(permission);
		}
	}

	public void addChildPermissions(SecurityPermissionDTO[]... permissionArrays) {
		if (null == permissionArrays || permissionArrays.length == 0) {
			throw new IllegalArgumentException();
		}
		if (null == childPermissions) {
			childPermissions = new ArrayList<HierarchyPermission>();
			hasChildren = true;
		}
		for (SecurityPermissionDTO[] permissionArray : permissionArrays) {
			for (SecurityPermissionDTO permission : permissionArray) {
				permission.setLevel(getLevel() + 1);
				permission.setParent(this);
				childPermissions.add(permission);
			}
		}
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SecurityPermissionDTO other = (SecurityPermissionDTO) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
}