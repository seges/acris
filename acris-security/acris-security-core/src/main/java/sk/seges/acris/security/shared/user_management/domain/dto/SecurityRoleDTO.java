package sk.seges.acris.security.shared.user_management.domain.dto;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;

/**
 * Security role serves as the holder of authorities for specific user (or any other entity). It is the entity grouping
 * authorities but the security mechanism is not dependent on it directly (nor the user). Security role might be used in
 * custom user service implementation where you can model user <-> role <-> authority relation.
 */
public class SecurityRoleDTO implements RoleData {
	private static final long serialVersionUID = 5356058807001610271L;

	public SecurityRoleDTO() {
	}

	private Integer id;

	private String name;

	private String description;

	private List<String> selectedAuthorities;
	
	private String webId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSelectedAuthorities() {
		return selectedAuthorities;
	}

	public void setSelectedAuthorities(List<String> securityPermissions) {
		this.selectedAuthorities = securityPermissions;
	}

	@Override
	public String getWebId() {
		return webId;
	}

	@Override
	public void setWebId(String webId) {
		this.webId = webId;
	}
	
	@Override
	public String toString() {
		return name;
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
		SecurityRoleDTO other = (SecurityRoleDTO) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

}