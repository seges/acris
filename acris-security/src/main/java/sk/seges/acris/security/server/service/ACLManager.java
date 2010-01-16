package sk.seges.acris.security.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.MutableAcl;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.server.annotations.RunAs;
import sk.seges.acris.security.server.dao.acl.IACLEntryDAO;
import sk.seges.acris.security.server.permission.ExtendedPermission;

@Component
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class ACLManager {

	@Autowired
	private MutableAclService aclService;

	@Autowired
	private IACLEntryDAO aclEntryDao;
	
    protected AclCache aclCache;

	public void removeACLEntries(ISecuredObject securedObject) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		removeACLEntries(securedObject, new PrincipalSid(authentication));
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeObjectIdentity(ISecuredObject securedObject) {
		ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(securedObject.getClass().getName(), securedObject.getId());
		aclService.deleteAcl(objectIdentity, false);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void removeACLEntries(ISecuredObject securedObject, Sid sid) {
		ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(securedObject);
		aclEntryDao.deleteByIdentityIdAndSid(securedObject, sid);
		aclCache.evictFromCache(objectIdentity);
		aclService.readAclById(objectIdentity); //update cache
	}

	@RunAs("ACL_MAINTENANCE_GENERAL_CHANGES")
	public void setACLEntries(ISecuredObject securedObject, sk.seges.acris.security.rpc.domain.Permission[] authorities) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		setACLEntries(securedObject, new PrincipalSid(authentication), authorities);
	}
	
	@RunAs("ACL_MAINTENANCE_GENERAL_CHANGES")
	public void setACLEntries(ISecuredObject securedObject, UserDetails user, sk.seges.acris.security.rpc.domain.Permission[] authorities) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setACLEntries(securedObject, sid, authorities);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void setACLEntries(ISecuredObject securedObject, Sid sid, sk.seges.acris.security.rpc.domain.Permission[] authorities) {

		MutableAcl acl = null;
		
		try {
			acl = (MutableAcl) aclService.readAclById(new ObjectIdentityImpl(securedObject));
		} catch (NotFoundException ex) {
			acl = aclService.createAcl(new ObjectIdentityImpl(securedObject));
		}
		
		for (sk.seges.acris.security.rpc.domain.Permission authority : authorities) {

			boolean found = false;

			for (AccessControlEntry entry : acl.getEntries()) {
				if (!entry.getSid().equals(sid)) {
					continue;
				}
				
				Permission permission = entry.getPermission();

				if (permission.getMask() == authority.getMask()) {
					found = true;
					break;
				}
			}

			if (!found) {
				acl.insertAce(0, getPermission(authority.getMask()),sid, true);
			}
		}

		aclService.updateAcl(acl);
	}
	
	private Permission getPermission(int mask) {
		//BasePermission.buildFromMask(mask) - Does not work?
		if (mask == BasePermission.CREATE.getMask()) {
			return BasePermission.CREATE;
		} else if (mask == BasePermission.DELETE.getMask()) {
			return BasePermission.DELETE;
		} else if (mask == BasePermission.READ.getMask()) {
			return BasePermission.READ;
		} else if (mask == BasePermission.WRITE.getMask()) {
			return BasePermission.WRITE;
		} else if (mask == BasePermission.ADMINISTRATION.getMask()) {
			return BasePermission.ADMINISTRATION;
		} else if (mask == ExtendedPermission.SEARCH.getMask()) {
			return ExtendedPermission.SEARCH;
		}

		return null;
	}

	public void setAclCache(AclCache aclCache) {
		this.aclCache = aclCache;
	}
}