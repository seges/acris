package sk.seges.acris.security.server.spring.acl.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import sk.seges.acris.security.server.utils.SecuredClassHelper;
import sk.seges.sesam.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.domain.IDomainObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public void removeAclRecords(Class<? extends ISecuredObject<?>> securedClass, UserData user) {
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
		Class<? extends ISecuredObject<?>> clazz = SecuredClassHelper.getSecuredClass(className);
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
	private Class<? extends ISecuredObject<?>> getSecuredSuperClass(Class<? extends ISecuredObject<?>> clazz) {
		return (Class<? extends ISecuredObject<?>>) clazz.getSuperclass();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void removeAclRecords(Class<? extends ISecuredObject<?>> securedClass, SpringAclSid sid) {
		// we need to remove also the superclass object identity ACLs
		Class<? extends ISecuredObject<?>> superClass = securedClass;
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
	public void removeAcl(ISecuredObject<?> securedObject) {
		ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(securedObject.getClass(),
				securedObject.getIdForACL());
		aclCache.evictFromCache(objectIdentity);
		aclService.deleteAcl(objectIdentity, false);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, getSidFromContext(), permissions);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		setAclRecords(securedObject, getSidFromContext(), permissions, updateParent);
	}
	
	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, user, permissions, true);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(securedObject, sid, permissions, updateParent);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, RoleData role,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, role, permissions, true);
	}

	@Override
	public void setAclRecords(ISecuredObject<?> securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		setAclRecords(securedObject, sid, permissions, updateParent);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		resetAclRecords(objectClass, aclId, sid, permissions);
	}

	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, RoleData role,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		resetAclRecords(objectClass, aclId, sid, permissions);
	}
	
	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, String userName,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(userName);
		resetAclRecords(objectClass, aclId, sid, permissions);
	}

	private void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long securedId, Sid sid,
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

		for (int i = 0; i < acl.getEntries().size(); i++) {
			acl.deleteAce(i);
		}
		acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
		acl.setOwner(sid);

		aclService.updateAcl(acl);
	}

	private AclSecuredObjectIdentityData getParentObjectIdentity(Class<? extends ISecuredObject<?>> objectClass, Long aclId) {
		
		AclSecuredClassDescriptionData aclClass = aclSecuredClassDescriptionDao.load(objectClass.getName());
		
//		if (aclClass == null) {
//			return null;
//		}
		AclSecuredObjectIdentityData result = aclObjectIdentityDao.findByObjectId(aclClass == null ? -1 : aclClass.getId(), aclId);

		if (result != null && result.getParentObject() != null) {
			return result.getParentObject();
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAclRecords(ISecuredObject<?> securedObject, Sid sid,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, sid, permissions, true);
	}
	
	@Override
	public void setAclRecords(
			Class<? extends ISecuredObject<?>> objectClass,
			Long aclId,
			UserData user,
			sk.seges.acris.security.shared.user_management.domain.Permission[] authorities) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(objectClass, aclId, null, sid, authorities, false);
	}

	private void setAclRecords(ISecuredObject<?> securedObject, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
		Class<?> clazz = securedObject.getClass();
		ISecuredObject<?> securedParent = securedObject.getParent();
		
		setAclRecords(clazz, securedObject.getIdForACL(), securedParent, sid, permissions, updateParent);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAclRecords(Class<?> clazz, Long aclId, ISecuredObject<?> securedParent, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent) {
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
			identity = new ObjectIdentityImpl(securedParent.getClass(), securedParent.getIdForACL());
			
			parentAcl = getOrCreateParentAcl(securedParent, sid, permissions, identity);
			acl.setParent(parentAcl);
			if (parentAcl.getEntries() == null || parentAcl.getEntries().size() <= 0) {
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

	private MutableAcl getOrCreateParentAcl(ISecuredObject<?> securedParent, Sid sid,
			sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, ObjectIdentity identity) {
		try {
			MutableAcl parentAcl = (MutableAcl) aclService.readAclById(identity);
			return parentAcl;
		} catch (NotFoundException e) {
			logger.info("No parent with aclId: " + identity.getIdentifier().toString() + " and class: "
					+ identity.getClass().getName() + " not exist, it will be created! ");
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

}