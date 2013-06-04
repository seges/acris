package sk.seges.sesam.security.server.model.acl;

import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.provider.acl.AclSecurityDataProvider;
import sk.seges.sesam.security.server.provider.acl.DefaultAclSecurityDataProvider;

public class AclDataRegistry {

	private Map<Class<?>, AclSecurityDataProvider<?>> aclSecurityDataProviderMap = new HashMap<Class<?>, AclSecurityDataProvider<?>>();
	private AclSecurityDataProvider<IDomainObject<?>> defaultAclSecurityDataProvider = new DefaultAclSecurityDataProvider();
	
	public <T extends AclSecurityDataProvider<? extends IDomainObject<?>>> AclDataRegistry register(Class<?> clazz, T provider) {
		aclSecurityDataProviderMap.put(clazz, provider);
		return this;
	}
	
	public void setDefaultAclSecurityDataProvider(AclSecurityDataProvider<IDomainObject<?>> defaultAclSecurityDataProvider) {
		this.defaultAclSecurityDataProvider = defaultAclSecurityDataProvider;
	}
	
	public AclSecurityData getAclSecurityData(IDomainObject<?> entity) {
		if (entity == null) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		AclSecurityDataProvider<IDomainObject<?>> aclSecurityDataProvider = 
				(AclSecurityDataProvider<IDomainObject<?>>) aclSecurityDataProviderMap.get(entity.getClass().getCanonicalName());
		
		if (aclSecurityDataProvider == null) {
			aclSecurityDataProvider = defaultAclSecurityDataProvider;
		}
		
		if (aclSecurityDataProvider == null) {
			return null;
		}
		
		return aclSecurityDataProvider.getSecurityData(entity);
	}
}