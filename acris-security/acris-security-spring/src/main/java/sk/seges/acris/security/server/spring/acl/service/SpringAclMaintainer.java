package sk.seges.acris.security.server.spring.acl.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
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

import sk.seges.acris.security.server.acl.dao.IAclObjectIdentityDao;
import sk.seges.acris.security.server.acl.dao.IAclRecordDao;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.annotation.RunAs;
import sk.seges.acris.security.server.spring.acl.domain.api.SpringAclSid;
import sk.seges.acris.security.server.spring.acl.domain.dto.SpringAclSidDTO;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.domain.IDomainObject;

//@Component
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class SpringAclMaintainer implements AclManager {

    private static final String ACL_MAINTAINER_ROLE = "ACL_MAINTENANCE_GENERAL_CHANGES";

    private static final String HIBERNATE_PROXY_CLASSNAME_SEPARATOR = "$$";

    private static final Set<Class> topParentClasses = new HashSet<Class>();
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
	private IAclRecordDao aclEntryDao;
	
	@Autowired
	protected IAclObjectIdentityDao aclObjectIdentityDao;
	
    protected AclCache aclCache;

    protected SpringAclSid createPrincipalSid(String username) {
    	return new SpringAclSidDTO(username);
    }

    private SpringAclSid createPrincipalSid(Authentication authentication) {
    	return new SpringAclSidDTO(authentication);
    }

    public void removeAclRecords(Class<? extends ISecuredObject> securedClass, UserData user) {
        removeAclRecords(securedClass, createPrincipalSid(user.getUsername()));
    }
    
    public void removeAclRecords(Long aclId, String className, UserData user) {
        removeAclRecords(aclId, className, createPrincipalSid(user.getUsername()));
    }
    
	public void removeAclRecords(Long aclId, String className) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		removeAclRecords(aclId, className, createPrincipalSid(authentication));
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeSecuredObjectIdentity(Long aclId, String className) {
	  //we need to remove also the superclass object identity ACLs
		Class clazz = getClass(className);
        while(! isTopParentClass(clazz)) {
            if(isHibernateProxy(clazz)) {
            	clazz = clazz.getSuperclass();
                continue;
            }
            ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(clazz, aclId);
            aclCache.evictFromCache(objectIdentity);
            aclService.deleteAcl(objectIdentity, false);
            
            clazz = clazz.getSuperclass();
        }
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void removeAclRecords(Long aclId, String className, SpringAclSid sid) {
	    //we need to remove also the superclass object identity ACLs
		Class clazz = getClass(className);
	    while(! isTopParentClass(clazz)) {
            if(isHibernateProxy(clazz)) {
            	clazz = clazz.getSuperclass();
                continue;
            }
	        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(clazz, aclId);
	        aclEntryDao.deleteByIdentityIdAndSid(aclId, clazz, sid, clazz.getName());
	        aclCache.evictFromCache(objectIdentity);
	        aclService.readAclById(objectIdentity); //update cache
	        
	        clazz = clazz.getSuperclass();
	    }
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void removeAclRecords(Class<? extends ISecuredObject> securedClass, SpringAclSid sid) {
	    //we need to remove also the superclass object identity ACLs
	    Class superClass = securedClass;
        while(! isTopParentClass(superClass)) {
            if(isHibernateProxy(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
            aclEntryDao.deleteByClassnameAndSid(superClass, sid);
            List<AclEntry> entries = aclEntryDao.findByClassnameAndSid(superClass, sid);
            for(AclEntry entry : entries) {
                aclCache.evictFromCache(entry.getObjectIdentity());
                aclService.readAclById((ObjectIdentity)entry.getObjectIdentity()); //update cache
            }
            
            superClass = superClass.getSuperclass();
        }
	}

	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		setAclRecords(securedObject, getSidFromContext(), permissions);
	}

	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(securedObject, sid, permissions);
	}

	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject<?> securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		setAclRecords(securedObject, sid, permissions);
	}
	
	
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Long aclId, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		resetAclRecords(aclId, sid, permissions);
	}
	
	@Override
	@RunAs(ACL_MAINTAINER_ROLE)
	public void resetAclRecords(Long aclId, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		PrincipalSid sid = new PrincipalSid(role.getName());
		resetAclRecords(aclId, sid, permissions);
	}
	
	private void resetAclRecords(Long aclId, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		MutableAcl acl = null;
		AclSecuredObjectIdentity objectIdentity = getParentObjectIdentity(aclId);
		
		if (objectIdentity != null) {
			try {
				acl = (MutableAcl) aclService.readAclById(new ObjectIdentityImpl(objectIdentity.getJavaType(), aclId));
			} catch (NotFoundException e) {
				throw new SecurityException("Could not update acl entry for aclId: " + aclId + " sid: " + sid + " cause acl object identity not found!", e);
			}
		}
		
        int authorityMask = 0;
        for (sk.seges.acris.security.shared.user_management.domain.Permission authority : permissions) {
            authorityMask |= authority.getMask();
        }
        
    	for (int i = 0; i < acl.getEntries().length; i++) {
    		acl.deleteAce(i);
    	}
    	acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
        aclService.updateAcl(acl);
	}
	
	private AclSecuredObjectIdentity getParentObjectIdentity(Long aclId) {
		AclSecuredObjectIdentity result = aclObjectIdentityDao.findByObjectId(aclId);
		
		if (result.getParentObject() != null) {
			return result.getParentObject();
		}
		return result;
//		while (result.getParentObject() != null) {
//			result = result.getParentObject();
//		}
//		return result;	
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAclRecords(ISecuredObject<?> securedObject, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions) {
		MutableAcl acl = null;
		ISecuredObject<?> securedParent = securedObject.getParent();
		Class clazz = securedObject.getClass();
		//we are going to traverse the inheritance tree starting with the SecuredObject's classT
//		while(! isTopParentClass(clazz)) {
//            if(isHibernateProxy(clazz)) {
//            	clazz = clazz.getSuperclass();
//              continue;
//            }
		
		while (isHibernateProxy(clazz)) {
			clazz = clazz.getSuperclass();
		}
		
        ObjectIdentityImpl identity = new ObjectIdentityImpl(clazz, securedObject.getIdForACL());
        
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
        if (securedParent != null) {
        	identity = new ObjectIdentityImpl(securedParent.getClass(), securedParent.getIdForACL());

       		parentAcl = getOrCreateParentAcl(securedParent, sid, permissions, identity);
        	acl.setParent(parentAcl);
        	if (parentAcl.getEntries() == null || parentAcl.getEntries().length <= 0) {
        		parentAcl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
        		aclService.updateAcl(parentAcl);
        	}
        } else {
	        boolean found = false;
	        boolean exactMatch = false;
	        int aceIndex = 0;
	        for (AccessControlEntry entry : acl.getEntries()) {
	            if (!entry.getSid().equals(sid)) {
	                aceIndex++;
	                continue;
	            }
	                
	            Permission permission = entry.getPermission();
	
	            if ((permission.getMask() & authorityMask) > 0) {
	                found = true;
	                if(permission.getMask() == authorityMask) {
	                    exactMatch = true;
	                }
	                break;
	            }
	            aceIndex++;
	        }
	        
	        if (!found) {
	            acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
	        } else {
	            if(!exactMatch) {
	                acl.deleteAce(aceIndex);
	                acl.insertAce(0, permissionFactory.buildFromMask(authorityMask), sid, true);
	            }
	        }
        }
        aclService.updateAcl(acl);
        
		//Should we do it also for superclass?
        //now, move to the superclass
        //clazz = clazz.getSuperclass();
	}

	private MutableAcl getOrCreateParentAcl(ISecuredObject<?> securedParent, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, ObjectIdentity identity) {
    	try {
    		MutableAcl parentAcl = (MutableAcl) aclService.readAclById(identity);
    		return parentAcl;
    	} catch (NotFoundException e) {
    		logger.info("No parent with aclId: " + identity.getIdentifier().toString() + " and class: " + identity.getJavaType().getName() + " not exist, it will be created! ");
    		setAclRecords(securedParent, sid, permissions);
    		return getOrCreateParentAcl(securedParent, sid, permissions, identity);
		}
	}

	public void setAclCache(AclCache aclCache) {
		this.aclCache = aclCache;
	}

    private boolean isHibernateProxy(Class clazz) {
        return clazz.getName().contains(HIBERNATE_PROXY_CLASSNAME_SEPARATOR);
    }
    
    private boolean isTopParentClass(Class clazz) {
        return topParentClasses.contains(clazz);
    }

    private Class getClass(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
            String message = "Class " + className + " could not be found.";
            logger.error(message, e);
            throw new RuntimeException(message);
		}
		return clazz;
    }

    private Sid getSidFromContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		return new PrincipalSid(authentication);
    }
}