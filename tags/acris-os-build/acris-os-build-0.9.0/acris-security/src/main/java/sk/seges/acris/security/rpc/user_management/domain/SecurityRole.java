package sk.seges.acris.security.rpc.user_management.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.sf.gilead.pojo.java5.LightEntity;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.sesam.domain.IDomainObject;

/**
 * Security role serves as the holder of authorities for specific user (or any
 * other entity). It is the entity grouping authorities but the security
 * mechanism is not dependent on it directly (nor the user). Security role might
 * be used in custom user service implementation where you can model user <->
 * role <-> authority relation.
 */
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", initialValue = 1)
public class SecurityRole extends LightEntity implements IDomainObject<Integer> {

	public static final String PERMISSION = "USER_ROLE";

	private static final long serialVersionUID = 5356058807001610271L;
	public static final String A_NAME = "name";

	public SecurityRole() {}

	@Id
	@GeneratedValue(generator = "role_id_seq")
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	private String description;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> selectedPermissions;

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

	public List<String> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<String> securityPermissions) {
		this.selectedPermissions = securityPermissions;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityRole other = (SecurityRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
