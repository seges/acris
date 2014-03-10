/**
 * 
 */
package sk.seges.acris.security.server.core.user_management.domain.hibernate;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaUserPreferences;
import sk.seges.corpis.server.domain.user.server.model.data.UserPreferencesData;

import javax.persistence.*;
import java.util.List;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class HibernateUserWithAuthorities extends HibernateGenericUser {
	
	private static final long serialVersionUID = 6755045948801685615L;

	private List<String> selectedAuthorities;
	
	public HibernateUserWithAuthorities() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Column
	public String getDescription() {
		return super.getDescription();
	}

	@Column
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Column
	public String getUsername() {
		return super.getUsername();
	}

	@Column
	public String getPassword() {
		return super.getPassword();
	}
	
	@OneToOne(cascade = CascadeType.ALL, targetEntity = JpaUserPreferences.class)
	public UserPreferencesData getUserPreferences() {
		return super.getUserPreferences();
	}

	@Override
	@Transient
	public List<String> getUserAuthorities() {
		fillAuthorities();
		return super.getUserAuthorities();
	}
	
	@Override
	public void setUserAuthorities(List<String> authorities) {
		setSelectedAuthorities(authorities);
		super.setUserAuthorities(authorities);
	}

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List<String> getSelectedAuthorities() {
		return selectedAuthorities;
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		this.selectedAuthorities = selectedAuthorities;
	}
	
	protected void fillAuthorities() {
		if (getUserAuthorities() == null) {
			setUserAuthorities(selectedAuthorities);
		}
	}
}
