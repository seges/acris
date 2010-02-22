package sk.seges.acris.security.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.gilead.pojo.java5.LightEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.MutableAcl;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
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
import sk.seges.acris.security.server.domain.acl.ACLEntry;
import sk.seges.acris.security.server.permission.ExtendedPermission;

@Component
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class ACLManager {

    private static final String CGLIB_CLASSNAME_SEPARATOR = "$$";

    private static final Set<Class> topParentClasses = new HashSet<Class>();
    @Autowired
    private DefaultPermissionFactory permissionFactory;
    
    static {
        topParentClasses.add(Object.class);
        topParentClasses.add(LightEntity.class);
    }
    
	@Autowired
	private MutableAclService aclService;

	@Autowired
	private IACLEntryDAO aclEntryDao;
	
    protected AclCache aclCache;

    public void removeACLEntries(Class<? extends ISecuredObject> securedClass, UserDetails user) {
        removeACLEntries(securedClass, new PrincipalSid(user.getUsername()));
    }
    
    public void removeACLEntries(ISecuredObject securedObject, UserDetails user) {
        removeACLEntries(securedObject, new PrincipalSid(user.getUsername()));
    }
    
	public void removeACLEntries(ISecuredObject securedObject) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			throw new IllegalStateException("No authentication object is in security context. Unable to update ACL entries");
		}
		
		removeACLEntries(securedObject, new PrincipalSid(authentication));
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void removeObjectIdentity(ISecuredObject securedObject) {
	  //we need to remove also the superclass object identity ACLs
        Class superClass = securedObject.getClass();
        while(! isTopParentClass(superClass)) {
            if(isCglibWrapper(superClass)) {
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
	private void removeACLEntries(ISecuredObject securedObject, Sid sid) {
	    //we need to remove also the superclass object identity ACLs
	    Class superClass = securedObject.getClass();
	    while(! isTopParentClass(superClass)) {
            if(isCglibWrapper(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
	        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(superClass, securedObject.getId());
	        aclEntryDao.deleteByIdentityIdAndSid(securedObject, sid);
	        aclCache.evictFromCache(objectIdentity);
	        aclService.readAclById(objectIdentity); //update cache
	        
	        superClass = superClass.getSuperclass();
	    }
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void removeACLEntries(Class<? extends ISecuredObject> securedClass, Sid sid) {
	    //we need to remove also the superclass object identity ACLs
	    Class superClass = securedClass;
        while(! isTopParentClass(superClass)) {
            if(isCglibWrapper(superClass)) {
                superClass = superClass.getSuperclass();
                continue;
            }
            aclEntryDao.deleteByClassnamAndSid(superClass, sid);
            List<ACLEntry> entries = aclEntryDao.findByClassnameAndSid(superClass, sid);
            for(ACLEntry entry : entries) {
                aclCache.evictFromCache(entry.getObjectIdentity());
                aclService.readAclById(entry.getObjectIdentity()); //update cache
            }
            
            superClass = superClass.getSuperclass();
        }
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
		
		//we are goind to traverse the inheritance tree starting with the SecuredObject's class
		Class superClass = securedObject.getClass();
		while(! isTopParentClass(superClass)) {
            if(isCglibWrapper(superClass)) {
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
	        for (sk.seges.acris.security.rpc.domain.Permission authority : authorities) {
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

    private boolean isCglibWrapper(Class clazz) {
        return clazz.getName().contains(CGLIB_CLASSNAME_SEPARATOR);
    }
    
    private boolean isTopParentClass(Class clazz) {
        return topParentClasses.contains(clazz);
    }
}