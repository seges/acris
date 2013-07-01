package sk.seges.acris.security.server.spring.acl.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.MutableAcl;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.core.acl.dao.api.IAclObjectIdentityDao;
import sk.seges.acris.security.server.core.acl.dao.api.IAclRecordDao;
import sk.seges.acris.security.server.core.acl.dao.api.IAclSecuredClassDescriptionDao;
import sk.seges.acris.security.server.core.annotation.RunAs;
import sk.seges.acris.security.server.spring.acl.domain.api.SpringAclSid;
import sk.seges.acris.security.server.spring.acl.domain.dto.SpringAclSidDTO;
import sk.seges.acris.security.server.spring.acl.sid.SidNameResolver;
import sk.seges.acris.security.server.utils.SecuredClassHelper;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.model.acl.AclSecurityData;
import sk.seges.sesam.security.shared.model.api.PermissionData;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SpringAclMaintainer implements AclManager {

	private static final String ACL_MAINTAINER_ROLE = "ACL_MAINTENANCE_GENERAL_CHANGES";

	private static final String HIBERNATE_PROXY_CLASSNAME_SEPARATOR = "$$";

	private static final Set<Class<?>> topParentClasses = new HashSet<Class<?>>();

	@Autowired
	private DefaultPermissionFactory permissionFactory;

	private static final Logger logger = Logger.getLogger(SpringAclMaintainer.class);

	static {
		topParentClasses.add(Object.class);
		topParentClasses.add(IDomainObject.class);
	}

	@Autowired
	private MutableAclService aclService;

	@Autowired
	@Qualifier(value = "aclRecordDao")
	private IAclRecordDao<?> aclEntryDao;

	@Autowired
	protected IAclObjectIdentityDao<?> aclObjectIdentityDao;

	@Autowired
	protected IAclSecuredClassDescriptionDao<?> aclSecuredClassDescriptionDao;

	protected AclCache aclCache;

	protected SpringAclSid createPrincipalSid(String username) {
		return new SpringAclSidDTO(username);
	}

	private SpringAclSid createPrincipalSid(Authentication authentication) {
		return new SpringAclSidDTO(authentication);
	}

	@Override
	public void removeAclRecords(Class<? extends IDomainObject<?>> securedClass, UserData user) {
		removeAclRecords(securedClass, createPrincipalSid(user.getUsername()));
	}

	@Override
	public void removeAclRecords(Long aclId, String className, UserData user) {
		removeAclRecords(aclId, className, createPrincipalSid(user.getUsername()));
	}

	@Override
	public void removeAclRecords(Long aclId, String className) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new IllegalStateException(
					"No authentication object is in security context. Unable to update ACL entries");
		}

		removeAclRecords(aclId, className, createPrincipalSid(authentication));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeSecuredObjectIdentity(Long aclId, String className) {
		removeAclRecords(aclId, className, (SpringAclSid)null);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void removeAclRecords(Long aclId, String className, SpringAclSid sid) {
		// we need to remove also the superclass object identity ACLs
		Class<? extends IDomainObject<?>> clazz = SecuredClassHelper.getSecuredClass(className);
		while (!isTopParentClass(clazz)) {
			if (isHibernateProxy(clazz)) {
				clazz = getSecuredSuperClass(clazz);
				continue;
			}
			ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(clazz, aclId);
			if (sid != null) {
				aclEntryDao.deleteByIdentityIdAndSid(aclId, clazz, sid, clazz.getName());
			} else {
				aclService.deleteAcl(objectIdentity, false);
			}
			aclEntryDao.deleteByIdentityIdAndSid(aclId, clazz, sid);
			aclCache.evictFromCache(objectIdentity);
			//aclService.readAclById(objectIdentity); // update cache

			clazz = getSecuredSuperClass(clazz);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IDomainObject<?>> getSecuredSuperClass(Class<? extends IDomainObject<?>> clazz) {
		return (Class<? extends IDomainObject<?>>) clazz.getSuperclass();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void removeAclRecords(Class<? extends IDomainObject<?>> securedClass, SpringAclSid sid) {
		// we need to remove also the superclass object identity ACLs
		Class<? extends IDomainObject<?>> superClass = securedClass;
		while (!isTopParentClass(superClass)) {
			if (isHibernateProxy(superClass)) {
				superClass = getSecuredSuperClass(superClass);
				continue;
			}
			aclEntryDao.deleteByClassnameAndSid(superClass, sid);
			List<AclEntryData> entries = aclEntryDao.findByClassnameAndSid(superClass, sid);
			for (AclEntryData entry : entries) {
				aclCache.evictFromCache(entry.getObjectIdentity());
				aclService.readAclById((ObjectIdentity) entry.getObjectIdentity()); // update
																					// cache
			}

			superClass = getSecuredSuperClass(superClass);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeAcl(AclSecurityData securedObject) {
		ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(securedObject.getAclClass(),
				securedObject.getAclId());
		aclCache.evictFromCache(objectIdentity);
		aclService.deleteAcl(objectIdentity, false);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(AclSecurityData securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, getSidFromContext(), permissions);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(AclSecurityData securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		setAclRecords(securedObject, getSidFromContext(), permissions, updateParent);
	}
	
	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(AclSecurityData securedObject, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, user, permissions, true);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(AclSecurityData securedObject, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(securedObject, sid, permissions, updateParent);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(AclSecurityData securedObject, RoleData role,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, role, permissions, true);
	}

	@Override
	public void setAclRecords(AclSecurityData securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		setAclRecords(securedObject, sid, permissions, updateParent);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		resetAclRecords(objectClass, aclId, sid, permissions);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, RoleData role,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		resetAclRecords(objectClass, aclId, sid, permissions);
	}
	
	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, String userName,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(userName);
		resetAclRecords(objectClass, aclId, sid, permissions);
	}

	private void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long securedId, Sid sid,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		MutableAcl acl = null;
		AclSecuredObjectIdentityData objectIdentity = getParentObjectIdentity(objectClass, securedId);
		if (objectIdentity == null) {
			throw new SecurityException("Could not update acl entry for aclId: " + securedId + " sid: " + sid
					+ " cause acl object identity not found!");
		}

		try {
			acl = (MutableAcl) aclService.readAclById(new ObjectIdentityImpl(objectIdentity.getJavaType(), securedId));
		} catch (NotFoundException e) {
			throw new SecurityException("Could not update acl entry for aclId: " + securedId + " sid: " + sid
					+ " cause acl object identity not found!", e);
		}

		int authorityMask = 0;
		for (sk.seges.acris.security.shared.user_management.domain.Permission authority : permissions) {
			authorityMask |= authority.getMask();
		}

		for (int i = 0; i < acl.getEntries().length; i++) {
			acl.deleteAce(i);
		}
		acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
		acl.setOwner(sid);

		aclService.updateAcl(acl);
	}

	private AclSecuredObjectIdentityData getParentObjectIdentity(Class<? extends IDomainObject<?>> objectClass, Long aclId) {
		
		AclSecuredClassDescriptionData aclClass = aclSecuredClassDescriptionDao.load(objectClass);
		
		AclSecuredObjectIdentityData result = aclObjectIdentityDao.findByObjectId(aclClass == null ? -1 : aclClass.getId(), aclId);

		if (result != null && result.getParentObject() != null) {
			return result.getParentObject();
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAclRecords(AclSecurityData securedObject, Sid sid,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, sid, permissions, true);
	}
	
	@Override
	public void setAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId,
			UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] authorities) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(objectClass, aclId, null, sid, authorities, false);
	}

	private void setAclRecords(AclSecurityData securedObject, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		Class<?> clazz = securedObject.getAclClass();
		AclSecurityData securedParent = securedObject.getParentAcl();
		
		setAclRecords(clazz, securedObject.getAclId(), securedParent, sid, permissions, updateParent);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAclRecords(Class<?> clazz, Long aclId, AclSecurityData securedParent, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		MutableAcl acl = null;
		

		while (isHibernateProxy(clazz)) {
			clazz = clazz.getSuperclass();
		}

		ObjectIdentityImpl identity = new ObjectIdentityImpl(clazz, aclId);

		try {
			acl = (MutableAcl) aclService.readAclById(identity);
		} catch (NotFoundException ex) {
			acl = aclService.createAcl(identity);
		}

		int authorityMask = 0;
		for (sk.seges.acris.security.shared.user_management.domain.Permission authority : permissions) {
			authorityMask |= authority.getMask();
		}

		MutableAcl parentAcl = null;
        if (securedParent != null && updateParent) {
			identity = new ObjectIdentityImpl(securedParent.getAclClass(), securedParent.getAclId());
			
			parentAcl = getOrCreateParentAcl(securedParent, sid, permissions, identity);
			acl.setParent(parentAcl);
			if (parentAcl.getEntries() == null || parentAcl.getEntries().length <= 0) {
				parentAcl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
				aclService.updateAcl(parentAcl);
			}
		} else {
			boolean found = false;
//			boolean exactMatch = false;
			int aceIndex = 0;
			for (AccessControlEntry entry : acl.getEntries()) {
				if (!entry.getSid().equals(sid)) {
					aceIndex++;
					continue;
				}

				Permission permission = entry.getPermission();

				if ((permission.getMask() & authorityMask) > 0) {
					found = true;
					if (permission.getMask() == authorityMask) {
//						exactMatch = true;
					}
					break;
				}
				aceIndex++;
			}

			if (!found) {
				acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
			} else {
//				if (!exactMatch) {
				acl.deleteAce(aceIndex);
				acl.insertAce(aceIndex, permissionFactory.buildFromMask(authorityMask), sid, true);
//				}
			}
		}
		aclService.updateAcl(acl);

		// Should we do it also for superclass?
		// now, move to the superclass
		// clazz = clazz.getSuperclass();
	}

	private MutableAcl getOrCreateParentAcl(AclSecurityData securedParent, Sid sid,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, ObjectIdentity identity) {
		try {
			MutableAcl parentAcl = (MutableAcl) aclService.readAclById(identity);
			return parentAcl;
		} catch (NotFoundException e) {
			logger.info("No parent with aclId: " + identity.getIdentifier().toString() + " and class: "
					+ identity.getJavaType().getName() + " not exist, it will be created! ");
			setAclRecords(securedParent, sid, permissions);
			return getOrCreateParentAcl(securedParent, sid, permissions, identity);
		}
	}

	public void setAclCache(AclCache aclCache) {
		this.aclCache = aclCache;
	}

	private boolean isHibernateProxy(Class<?> clazz) {
		return clazz.getName().contains(HIBERNATE_PROXY_CLASSNAME_SEPARATOR);
	}

	private boolean isTopParentClass(Class<?> clazz) {
		return topParentClasses.contains(clazz);
	}


	private Sid getSidFromContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new IllegalStateException(
					"No authentication object is in security context. Unable to update ACL entries");
		}
		return new PrincipalSid(authentication);
	}

	@Override
	public PermissionData getPermissionData(AclSecurityData aclSecurityData) {
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(aclSecurityData.getClassName(), aclSecurityData.getAclId());
		Acl acl = aclService.readAclById(objectIdentity);

		PermissionData permission = new PermissionData();
		if (acl == null || acl.getEntries() == null || acl.getEntries().length == 0) {
			permission.setVisible(false);
			return permission;
		}

		permission.setVisible(true);

		for (AccessControlEntry aclEntry: acl.getEntries()) {
			//using just last role name
			//TODO list of role name should be better
			//TODO or much better distinguish between roles and principals
			permission.setRoleName(SidNameResolver.getSidName(aclEntry.getSid()));
		}

		return permission;
	}
}