package sk.seges.acris.security.server.core.user_management.domain.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import sk.seges.acris.security.shared.user_management.domain.dto.SecurityRoleDTO;

/**
 * Security role serves as the holder of authorities for specific user (or any other entity). It is the entity grouping
 * authorities but the security mechanism is not dependent on it directly (nor the user). Security role might be used in
 * custom user service implementation where you can model user <-> role <-> authority relation.
 */
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", initialValue = 1)
public class JpaSecurityRole extends SecurityRoleDTO {
	
	private static final long serialVersionUID = 76070776260643849L;

	public static final String GRANT = "USER_ROLE";

	public JpaSecurityRole() {
	}

	@Id
	@GeneratedValue(generator = "role_id_seq")
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "name", nullable = false, unique = true)
	public String getName() {
		return super.getName();
	}

	@Column
	public String getDescription() {
		return super.getDescription();
	}

	@OneToMany(fetch=FetchType.EAGER)
	private Set<JpaStringEntity> authorities = new HashSet<JpaStringEntity>();
	
	@Transient
	public List<String> getSelectedAuthorities() {
		return super.getSelectedAuthorities();
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		authorities.clear();
		for (String selectedAuthority: selectedAuthorities) {
			authorities.add(new JpaStringEntity(selectedAuthority));
		}
		super.setSelectedAuthorities(selectedAuthorities);
	}
}