package sk.seges.acris.security.server.spring.acl.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSid;
import sk.seges.acris.security.server.spring.acl.domain.api.SpringAclSid;
import sk.seges.acris.security.server.spring.user_management.domain.SpringUserAdapter;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

/**
 * The table ACL_SID essentially lists all the users in our systems
 */
@Entity
@Table(name = "ACL_SID", uniqueConstraints = {@UniqueConstraint(columnNames = {AclSidData.SID, AclSidData.PRINCIPAL})})
public class JpaSpringAclSid extends JpaAclSid implements SpringAclSid {

	private static final long serialVersionUID = -4481194979683240941L;

	public JpaSpringAclSid(String principal) {
		Assert.hasText(principal, "Principal required");
		setSid(principal);
		setPrincipal(true);
	}

	public JpaSpringAclSid(Authentication authentication) {
		Assert.notNull(authentication, "Authentication required");
		Assert.notNull(authentication.getPrincipal(), "Principal required");

		if (authentication.getPrincipal() instanceof UserDetails) {
			setSid(((UserDetails) authentication.getPrincipal()).getUsername());
		} else if (authentication.getPrincipal() instanceof UserData) {
			setSid(new SpringUserAdapter((UserData)authentication.getPrincipal()).getUsername());
		} else {
			setSid(authentication.getPrincipal().toString());
		}
		setPrincipal(true);
	}

}
