package sk.seges.acris.security.server.spring.acl.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.Permission;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationCollectionFilteringProvider;

import sk.seges.acris.security.shared.exception.SecurityException;

public class BetterAclEntryAfterInvocationCollectionFilteringProvider extends AclEntryAfterInvocationCollectionFilteringProvider {

    public BetterAclEntryAfterInvocationCollectionFilteringProvider(AclService aclService, Permission[] requirePermission) {
        super(aclService, requirePermission);
    }
    
    @Override
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
    		Object returnedObject) throws AccessDeniedException {
    	Object result = super.decide(authentication, object, config, returnedObject);
    	
    	if (returnedObject != null && (result == null || isEmpty(result))) {
    		throw new SecurityException("User does not have permission for object: " + object + " returned object: " + returnedObject);
    	}
    	
    	return result;
    }
    
    @SuppressWarnings("rawtypes")
	private boolean isEmpty(Object result) {
    	if (result instanceof ArrayList) {
    		return ((ArrayList)result).size() <= 0;
    	} else if (result instanceof Collection) {
    		return ((Collection)result).size() <= 0;
    	}
    	
    	throw new SecurityException("A Collection or an array (or null) was required as the "
                 + "returnedObject, but the returnedObject was: " + result);
    }
}
