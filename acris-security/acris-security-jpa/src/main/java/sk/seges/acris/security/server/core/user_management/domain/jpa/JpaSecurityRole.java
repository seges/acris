package sk.seges.acris.security.server.core.user_management.domain.jpa;

import sk.seges.corpis.server.domain.user.server.model.base.RoleBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Security role serves as the holder of authorities for specific user (or any other entity). It is the entity grouping
 * authorities but the security mechanism is not dependent on it directly (nor the user). Security role might be used in
 * custom user service implementation where you can model user <-> role <-> authority relation.
 */
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", initialValue = 1)
public class JpaSecurityRole extends RoleBase {
	
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
	public List<JpaStringEntity> getAuthorities() {
		List<JpaStringEntity> authorities = new ArrayList<JpaStringEntity>();
		for (String authority: super.getSelectedAuthorities()) {
			JpaStringEntity entity = new JpaStringEntity();
			entity.setId(getId().longValue());
			entity.setValue(authority);
			authorities.add(entity);
		}
		return authorities;
	}

	public void setAuthorities(List<JpaStringEntity> authorities) {
		List<String> result = new ArrayList<String>();
		for (JpaStringEntity authority: authorities) {
			result.add(authority.getValue());
		}
		super.setSelectedAuthorities(result);
	}
}