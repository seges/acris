/**
 * 
 */
package sk.seges.acris.security.shared.core.user_management.domain.hibernate;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.acris.security.shared.core.user_management.domain.jpa.JpaUserPreferences;
import sk.seges.acris.security.shared.user_management.domain.api.UserPreferences;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class HibernateUserWithAuthorities extends GenericUserDTO {
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
	public UserPreferences getUserPreferences() {
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

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@Column(name = "element")
	@CollectionTable(name = "user_with_authorities_selectedauthorities", joinColumns = @JoinColumn(name = "user_with_authorities_id"))
	public List<String> getSelectedAuthorities() {
		return selectedAuthorities;
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		this.selectedAuthorities = selectedAuthorities;
	}
	
	protected void fillAuthorities() {
		if(authorities == null) {
			authorities = selectedAuthorities;
		}
	}
}
