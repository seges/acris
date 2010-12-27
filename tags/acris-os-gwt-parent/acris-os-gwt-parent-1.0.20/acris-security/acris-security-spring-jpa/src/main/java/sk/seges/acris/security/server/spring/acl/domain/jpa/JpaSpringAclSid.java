package sk.seges.acris.security.server.spring.acl.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSid;
import sk.seges.acris.security.server.core.acl.domain.api.AclSidBeanWrapper;
import sk.seges.acris.security.server.spring.acl.domain.api.SpringAclSid;

/**
 * The table ACL_SID essentially lists all the users in our systems
 */
@Entity
@Table(name = "ACL_SID", uniqueConstraints = {@UniqueConstraint(columnNames = {AclSidBeanWrapper.SID, AclSidBeanWrapper.PRINCIPAL})})
public class JpaSpringAclSid extends JpaAclSid implements SpringAclSid {

	private static final long serialVersionUID = -4481194979683240941L;

}
