package sk.seges.acris.security.server.spring.acl.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.Permission;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationCollectionFilteringProvider;

import sk.seges.acris.security.server.spring.annotation.processor.DefaultSecurityAnnotationProcessor;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.sesam.dao.PagedResult;

public class BetterAclEntryAfterInvocationCollectionFilteringProvider extends
		AclEntryAfterInvocationCollectionFilteringProvider {

	public BetterAclEntryAfterInvocationCollectionFilteringProvider(AclService aclService,
			Permission[] requirePermission) {
		super(aclService, requirePermission);
	}

	@Override
	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
			Object returnedObject) throws AccessDeniedException {
		Iterator iter = config.getConfigAttributes().iterator();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();

			if (DefaultSecurityAnnotationProcessor.AFTER_ACL_COLLECTION_READ_TOKEN.equals(attribute.getAttribute())) {
				// we have to check it before decission!
				boolean isEmpty = returnedObject != null && !isEmpty(returnedObject);

				Object result = super.decide(authentication, object, config, returnedObject);

				if (isEmpty && (result == null || isEmpty(result))) {
					throw new SecurityException("User does not have permission for object: " + object
							+ " returned object: " + returnedObject);
				}

				return result;
			}
		}
		return returnedObject;

	}

	@SuppressWarnings("rawtypes")
	private boolean isEmpty(Object result) {
		if (result instanceof ArrayList) {
			return ((ArrayList) result).size() <= 0;
		} else if (result instanceof Collection) {
			return ((Collection) result).size() <= 0;
		} else if (result instanceof PagedResult<?>) {
			if (((PagedResult<List<?>>) result).getResult() instanceof List<?>) {
				return ((PagedResult<List<?>>) result).getResult().size() <= 0;
			}
		}

		throw new SecurityException("A Collection or an array (or null) was required as the "
				+ "returnedObject, but the returnedObject was: " + result);
	}
}
