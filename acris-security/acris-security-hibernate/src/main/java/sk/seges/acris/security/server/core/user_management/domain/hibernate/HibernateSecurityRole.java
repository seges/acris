package sk.seges.acris.security.server.core.user_management.domain.hibernate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.corpis.server.domain.user.server.model.base.RoleBase;

/**
 * Security role serves as the holder of authorities for specific user (or any other entityprovider). It is the entityprovider grouping
 * authorities but the security mechanism is not dependent on it directly (nor the user). Security role might be used in
 * custom user service implementation where you can model user <-> role <-> authority relation.
 */
@Entity
@Table(name = "role", uniqueConstraints = { @UniqueConstraint(columnNames = {HibernateSecurityRole.WEB_ID, HibernateSecurityRole.NAME})})
@SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", initialValue = 1)
public class HibernateSecurityRole extends RoleBase {
	
	private static final long serialVersionUID = 76070776260643849L;

	public static final String GRANT = "USER_ROLE";

	public HibernateSecurityRole() {
	}

	@Override
	@Id
	@GeneratedValue(generator = "role_id_seq")
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "name", nullable = false)
	public String getName() {
		return super.getName();
	}

	@Override
	@Column
	public String getDescription() {
		return super.getDescription();
	}
	
	@Override
	@Column
	public String getWebId() {
		return super.getWebId();
	}

	@Override
	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List<String> getSelectedAuthorities() {
		return super.getSelectedAuthorities();
	}
}