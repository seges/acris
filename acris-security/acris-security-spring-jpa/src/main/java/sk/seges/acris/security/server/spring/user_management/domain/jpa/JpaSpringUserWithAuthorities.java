/**
 * 
 */
package sk.seges.acris.security.server.spring.user_management.domain.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaGenericUser;
import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaStringEntity;
import sk.seges.acris.security.server.spring.user_management.domain.SpringAuthoritiesSupport;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class JpaSpringUserWithAuthorities extends JpaGenericUser implements UserDetails {

	private static final long serialVersionUID = 6755045948801685615L;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<JpaStringEntity> selectedAuthorities = new HashSet<JpaStringEntity>();

	@Transient
	private List<String> originalAuthorities = new ArrayList<String>();

	@Transient
	private SpringAuthoritiesSupport springSupport = new SpringAuthoritiesSupport(this);

	public List<String> getSelectedAuthorities() {
		return originalAuthorities;
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		this.selectedAuthorities.clear();
		for (String authority : selectedAuthorities) {
			this.selectedAuthorities.add(new JpaStringEntity(authority));
		}
		this.originalAuthorities = selectedAuthorities;
	}

	private void fillAuthorities() {
		if (authorities == null) {
			authorities = originalAuthorities;
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
	
	public GrantedAuthority[] getAuthorities() {
		fillAuthorities();
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
		setSelectedAuthorities(this.authorities);
	}
}
