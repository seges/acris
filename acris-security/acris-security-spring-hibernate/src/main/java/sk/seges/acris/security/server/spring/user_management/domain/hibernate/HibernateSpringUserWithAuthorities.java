/**
 * 
 */
package sk.seges.acris.security.server.spring.user_management.domain.hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.server.hibernate.user_management.domain.HibernateUserWithAuthorities;
import sk.seges.acris.security.server.spring.user_management.domain.SpringAuthoritiesSupport;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class HibernateSpringUserWithAuthorities extends HibernateUserWithAuthorities implements UserDetails {

	private static final long serialVersionUID = 6104671878442128629L;

	@Transient
	private SpringAuthoritiesSupport springSupport = new SpringAuthoritiesSupport(this);

	@Override
	public GrantedAuthority[] getAuthorities() {
		fillAuthorities();
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
		setSelectedAuthorities(this.authorities);
	}
}