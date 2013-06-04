package sk.seges.acris.security.server.acl.vote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.AuthorizationServiceException;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.Permission;

public class AclEntryListVoter extends AclEntryVoter {

	public AclEntryListVoter(AclService aclService,
			String processConfigAttribute, Permission[] requirePermission) {
		super(aclService, processConfigAttribute, requirePermission);
	}

	protected Object[] getDomainObjectInstances(Object secureObject) {
    	List<Object> result = new ArrayList<Object>();
    	
        Object[] args;
        Class[] params;

        if (secureObject instanceof MethodInvocation) {
            MethodInvocation invocation = (MethodInvocation) secureObject;
            params = invocation.getMethod().getParameterTypes();
            args = invocation.getArguments();
        } else {
            JoinPoint jp = (JoinPoint) secureObject;
            params = ((CodeSignature) jp.getStaticPart().getSignature()).getParameterTypes();
            args = jp.getArgs();
        }

        for (int i = 0; i < params.length; i++) {
            if (Collection.class.isAssignableFrom(params[i]) && args[i] != null) {
            	Iterator iterator = ((Collection)args[i]).iterator();
            	while (iterator.hasNext()) {
            		Object listEntry = iterator.next();
            		if (listEntry != null) {
            			if (getProcessDomainObjectClass().isAssignableFrom(listEntry.getClass())) {
            				result.add(listEntry);
            			}
            		}
            	}
            } else if (params[i].isArray() && args[i] != null) {
            	for (Object arrayEntry : (Object[])args[i]) {
            		if (arrayEntry != null) {
            			if (getProcessDomainObjectClass().isAssignableFrom(arrayEntry.getClass())) {
            				result.add(arrayEntry);
            			}
            		}
            	}
            } else {
	            if (getProcessDomainObjectClass().isAssignableFrom(params[i])) {
	            	result.add(args[i]);
	            }
            }
        }

        if (result.size() == 0) {
	        throw new AuthorizationServiceException("Secure object: " + secureObject
	            + " did not provide any argument of type: " + getProcessDomainObjectClass());
        }
        
        return result.toArray(new Object[0]);
    }
}
