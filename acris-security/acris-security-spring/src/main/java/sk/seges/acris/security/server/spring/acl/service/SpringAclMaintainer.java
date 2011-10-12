package sk.seges.acris.security.server.spring.acl.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import sk.seges.acris.security.server.acl.dao.IAclRecordDao;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.annotation.RunAs;
import sk.seges.acris.security.server.spring.acl.domain.api.SpringAclSid;
import sk.seges.acris.security.server.spring.acl.domain.dto.SpringAclSidDTO;
import sk.seges.acris.security.shared.domain.ISecuredObject;
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
    
    static {
        topParentClasses.add(Object.class);
        topParentClasses.add(IDomainObject.class);
    }
    
	@Autowired
	private MutableAclService aclService;

	@Autowired
	@Qualifier(value = "aclRecordDao")
	private IAclRecordDao aclEntryDao;
	
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
    
    public void removeAclRecords(ISecuredObject securedObject, UserData user) {
        removeAclRecords(securedObject, createPrincipalSid(user.getUsername()));
    }
    
	public void removeAclRecords(ISecuredObject securedObject) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		removeAclRecords(securedObject, createPrincipalSid(authentication));
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeSecuredObjectIdentity(ISecuredObject securedObject) {
	  //we need to remove also the superclass object identity ACLs
        Class superClass = securedObject.getClass();
        while(! isTopParentClass(superClass)) {
            if(isHibernateProxy(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
            ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(superClass, securedObject.getId());
            aclCache.evictFromCache(objectIdentity);
            aclService.deleteAcl(objectIdentity, false);
            
            superClass = superClass.getSuperclass();
        }
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void removeAclRecords(ISecuredObject securedObject, SpringAclSid sid) {
	    //we need to remove also the superclass object identity ACLs
	    Class superClass = securedObject.getClass();
	    while(! isTopParentClass(superClass)) {
            if(isHibernateProxy(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
	        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(superClass, securedObject.getId());
	        aclEntryDao.deleteByIdentityIdAndSid(securedObject, sid, superClass.getName());
	        aclCache.evictFromCache(objectIdentity);
	        aclService.readAclById(objectIdentity); //update cache
	        
	        superClass = superClass.getSuperclass();
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
	public void setAclRecords(ISecuredObject securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] authorities) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		setAclRecords(securedObject, new PrincipalSid(authentication), authorities);
	}
	
	@RunAs(ACL_MAINTAINER_ROLE)
	public void setAclRecords(ISecuredObject securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] authorities) {
		PrincipalSid sid = new PrincipalSid(user.getUsername());
		setAclRecords(securedObject, sid, authorities);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void setAclRecords(ISecuredObject securedObject, Sid sid, sk.seges.acris.security.shared.user_management.domain.Permission[] authorities) {

		MutableAcl acl = null;
		
		//we are goind to traverse the inheritance tree starting with the SecuredObject's class
		Class superClass = securedObject.getClass();
		while(! isTopParentClass(superClass)) {
            if(isHibernateProxy(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
	        ObjectIdentityImpl identity = new ObjectIdentityImpl(superClass, securedObject.getId());

	        try {
	            acl = (MutableAcl) aclService.readAclById(identity);
	        } catch (NotFoundException ex) {
	            acl = aclService.createAcl(identity);
	        }
	        
	        int authorityMask = 0;
	        for (sk.seges.acris.security.shared.user_management.domain.Permission authority : authorities) {
	            authorityMask |= authority.getMask();
            }

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

	        aclService.updateAcl(acl);
	        
	        //now, move to the superclass
	        superClass = superClass.getSuperclass();
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
}