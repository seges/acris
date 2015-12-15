package 	sk.seges.acris.security.server.acl.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.util.LoggedUserRole;
import sk.seges.acris.security.server.utils.SecuredClassHelper;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.security.shared.domain.ISecuredObject;
import sk.seges.sesam.server.model.converter.ClassConverter;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

@LocalService
public class AclMaintenanceService implements IAclMaintenanceServiceLocal {

    private AclManager aclManager;
    
    private final ConverterProviderContext converterProviderContext;
    
    public AclManager getAclManager() {
        return aclManager;
    }

    public void setAclManager(AclManager aclManager) {
        this.aclManager = aclManager;
    }

    public AclMaintenanceService(ConverterProviderContext converterProviderContext, AclManager aclManager) {
    	this.aclManager = aclManager;
    	this.converterProviderContext = converterProviderContext;
    }
    
    public void removeACLEntries(UserData user, String[] securedClassNames) {
        for(String securedClassName : securedClassNames) {
            Class<? extends ISecuredObject<?>> securedClass = SecuredClassHelper.getSecuredClass(securedClassName);
            aclManager.removeAclRecords(securedClass, user);
        }
    }
    
    public void removeACLEntries(List<Long> aclIds, String className, UserData user) {
    	className=  ClassConverter.getDomainClassName(converterProviderContext, className);
    	for (Long id : aclIds) {
    		aclManager.removeAclRecords(id, className, user);
    	}
    }

    public void resetACLEntries(String className, Long aclId, UserData user, Permission[] authorities) {
    	className=  ClassConverter.getDomainClassName(converterProviderContext, className);
   		aclManager.resetAclRecords(SecuredClassHelper.getSecuredClass(className), aclId, user, authorities);
    }
    
	@Override
	public void resetACLEntries(String className, UserData user, Permission[] authorities, List<Long> aclIds) {
		for (Long aclId : aclIds) {
			resetACLEntries(className, aclId, user, authorities);
		}
	}

    @Override
    public void resetACLEntriesLoggedRole(String className, Long aclId, Permission[] authorities) {
    	className=  ClassConverter.getDomainClassName(converterProviderContext, className);
   		aclManager.resetAclRecords(SecuredClassHelper.getSecuredClass(className), aclId, new LoggedUserRole(), authorities);
    }

	@Override
	public void setAclEntries(String className, Long aclId, UserData user,	Permission[] authorities) {
    	className=  ClassConverter.getDomainClassName(converterProviderContext, className);
   		aclManager.setAclRecords(SecuredClassHelper.getSecuredClass(className), aclId, user, authorities);
	}

	@Override
	public void setAclEntries(Map<String, List<Long>> acls,
			UserData user, Permission[] authorities) {
		for(Entry<String, List<Long>> entry : acls.entrySet()) {
	    	String className = ClassConverter.getDomainClassName(converterProviderContext, entry.getKey());
			Class<? extends ISecuredObject<?>> securedClass = SecuredClassHelper.getSecuredClass(className);
	    	for (Long aclId : entry.getValue()) {
				aclManager.setAclRecords(securedClass, aclId, user, authorities);
	    	}
		}
	}

	@Override
	public boolean isVisibleFor(String sid, String className, Long aclId) {
		List<Sid> sids = null;
		try {
			sids = aclManager.loadSidNames(SecuredClassHelper.getSecuredClass(className), aclId);
		} catch (SecurityException e) {
			//it means secured object not found, so it should be visible
			return true;
		} 
		
		if (sids != null && !sids.isEmpty()) {
			for (Sid sidEntry : sids) {
				if ((sidEntry instanceof PrincipalSid && ((PrincipalSid)sidEntry).getPrincipal().equals(sid)) ||
						sidEntry instanceof GrantedAuthoritySid && ((GrantedAuthoritySid)sidEntry).getGrantedAuthority().equals(sid)) {
					return true;
				}
			}
			return false;
		} 
		return true;
	}
}