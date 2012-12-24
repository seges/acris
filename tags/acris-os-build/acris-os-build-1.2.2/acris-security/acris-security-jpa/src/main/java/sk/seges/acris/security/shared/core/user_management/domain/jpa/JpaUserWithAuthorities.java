/**
 * 
 */
package sk.seges.acris.security.shared.core.user_management.domain.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class JpaUserWithAuthorities extends JpaGenericUser {

	private static final long serialVersionUID = 6755045948801685615L;

	public JpaUserWithAuthorities() {	
	}
	
	private Set<JpaStringEntity> selectedAuthorities = new HashSet<JpaStringEntity>();

	@Transient
	private List<String> originalAuthorities = new ArrayList<String>();

	@OneToMany(fetch = FetchType.EAGER)
	public List<String> getSelectedAuthorities() {
		return originalAuthorities;
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		this.selectedAuthorities.clear();
		for (String authority : selectedAuthorities) {
			this.selectedAuthorities.add(new JpaStringEntity(getId(), authority));
		}
		this.originalAuthorities = selectedAuthorities;
	}

	private void fillAuthorities() {
		if (authorities == null) {
			authorities = originalAuthorities;
		}
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
