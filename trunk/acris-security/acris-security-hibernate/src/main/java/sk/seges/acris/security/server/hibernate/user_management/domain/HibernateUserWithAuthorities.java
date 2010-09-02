/**
 * 
 */
package sk.seges.acris.security.server.hibernate.user_management.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaGenericUser;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class HibernateUserWithAuthorities extends JpaGenericUser {
	private static final long serialVersionUID = 6755045948801685615L;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> selectedAuthorities;

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
	
	@Override
	public boolean hasAuthority(String authority) {
		fillAuthorities();
		return super.hasAuthority(authority);
	}
	
	@Override
	public List<String> getUserAuthorities() {
		fillAuthorities();
		return super.getUserAuthorities();
	}
	
	@Override
	public void setUserAuthorities(List<String> authorities) {
		setSelectedAuthorities(authorities);
		super.setUserAuthorities(authorities);
	}
}
