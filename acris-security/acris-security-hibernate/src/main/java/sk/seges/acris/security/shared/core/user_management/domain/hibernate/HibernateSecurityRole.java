package sk.seges.acris.security.shared.core.user_management.domain.hibernate;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.acris.security.shared.user_management.domain.dto.SecurityRoleDTO;

/**
 * Security role serves as the holder of authorities for specific user (or any other entity). It is the entity grouping
 * authorities but the security mechanism is not dependent on it directly (nor the user). Security role might be used in
 * custom user service implementation where you can model user <-> role <-> authority relation.
 */
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", initialValue = 1)
public class HibernateSecurityRole extends SecurityRoleDTO {
	
	private static final long serialVersionUID = 76070776260643849L;

	public static final String GRANT = "USER_ROLE";

	public HibernateSecurityRole() {
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

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@Column(name = "element")
	@CollectionTable(name = "role_selectedauthorities", joinColumns = @JoinColumn(name = "role_id"))
	public List<String> getSelectedAuthorities() {
		return super.getSelectedAuthorities();
	}
}