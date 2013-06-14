package 	sk.seges.acris.security.server.acl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.util.LoggedUserRole;
import sk.seges.acris.security.server.utils.SecuredClassHelper;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.security.server.model.acl.AclDataRegistry;
import sk.seges.sesam.security.server.model.acl.AclSecuredEntity;
import sk.seges.sesam.security.server.model.acl.AclSecurityData;
import sk.seges.sesam.server.model.converter.ClassConverter;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

@LocalService
public class AclMaintenanceService implements IAclMaintenanceServiceLocal {

    private final AclManager aclManager;
    private final ConverterProviderContext converterProviderContext;
    private final AclDataRegistry aclDataRegistry;
    
    public AclMaintenanceService(ConverterProviderContext converterProviderContext, AclManager aclManager, AclDataRegistry aclDataRegistry) {
    	this.aclManager = aclManager;
    	this.converterProviderContext = converterProviderContext;
    	this.aclDataRegistry = aclDataRegistry;
    }
    
    @Override
    public void removeACLEntries(UserData user, String[] securedClassNames) {
        for(String securedClassName : securedClassNames) {
            Class<? extends IDomainObject<?>> securedClass = SecuredClassHelper.getSecuredClass(securedClassName);
            aclManager.removeAclRecords(securedClass, user);
        }
    }
    
    private <T extends IDomainObject<?>> Long getAclId(AclSecuredEntity<T> securedObject) {
    	if (securedObject != null && securedObject.getAclData() != null) {
    		return securedObject.getAclData().getAclId();
    	}
    	
    	return null;
    }

    @SuppressWarnings("unchecked")
	private Class<? extends IDomainObject<?>> getSecuredClass(AclSecuredEntity<?> securedObject) {
    	return (Class<? extends IDomainObject<?>>) securedObject.getEntity().getClass();
    }

	@Override
    public void resetACLEntries(AclSecuredEntity<?> securedObject, UserData user, Permission[] authorities) {
   		aclManager.resetAclRecords(getSecuredClass(securedObject), getAclId(securedObject), user, authorities);
    }

    @Override
    public void resetACLEntriesLoggedRole(AclSecuredEntity<?> securedObject, Permission[] authorities) {
   		aclManager.resetAclRecords(getSecuredClass(securedObject), getAclId(securedObject), new LoggedUserRole(), authorities);
    }

	@Override
	public void setAclEntries(AclSecuredEntity<?> securedObject, UserData user, Permission[] authorities) {
   		aclManager.setAclRecords(getSecuredClass(securedObject), getAclId(securedObject), user, authorities);
	}

	@Override
	public void setAclEntries(Map<String, List<Long>> acls, UserData user, Permission[] authorities) {
		for(Entry<String, List<Long>> entry : acls.entrySet()) {
	    	String className = ClassConverter.getDomainClassName(converterProviderContext, entry.getKey());
			Class<? extends IDomainObject<?>> securedClass = SecuredClassHelper.getSecuredClass(className);
	    	for (Long aclId : entry.getValue()) {
				aclManager.setAclRecords(securedClass, aclId, user, authorities);
	    	}
		}
	}

	@Override
	public <DOMAIN_T extends IDomainObject<?>> List<AclSecuredEntity<DOMAIN_T>> fetchAclSecurityData(List<DOMAIN_T> domains) {
		
		List<AclSecuredEntity<DOMAIN_T>> result = new ArrayList<AclSecuredEntity<DOMAIN_T>>();
		
		for (DOMAIN_T domain: domains) {
			AclSecuredEntity<DOMAIN_T> securedEntity = new AclSecuredEntity<DOMAIN_T>();
			securedEntity.setEntity(domain);
			securedEntity.setAclData(aclDataRegistry.getAclSecurityData(domain));
			AclSecurityData securityData = new AclSecurityData(securedEntity.getAclData().getAclId(), securedEntity.getAclData().getClassName());
			securedEntity.setPermissionData(aclManager.getPermissionData(securityData));
			result.add(securedEntity);
		}
		
		return result;
	}

	@Override
	public <DOMAIN_T extends IDomainObject<?>> AclSecuredEntity<DOMAIN_T> fetchAclSecurityData(DOMAIN_T domain) {
		AclSecuredEntity<DOMAIN_T> securedEntity = new AclSecuredEntity<DOMAIN_T>();
		securedEntity.setEntity(domain);
		securedEntity.setAclData(aclDataRegistry.getAclSecurityData(domain));
		AclSecurityData securityData = new AclSecurityData(securedEntity.getAclData().getAclId(), securedEntity.getAclData().getClassName());
		securedEntity.setPermissionData(aclManager.getPermissionData(securityData));
		return securedEntity;
	}
}