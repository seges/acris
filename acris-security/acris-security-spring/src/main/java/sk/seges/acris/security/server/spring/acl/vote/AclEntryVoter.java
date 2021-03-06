package sk.seges.acris.security.server.spring.acl.vote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAclVoter;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import sk.seges.acris.security.server.spring.acl.sid.RolesPublicSidRetrievalStrategy;

public class AclEntryVoter extends AbstractAclVoter {

    private static final Log logger = LogFactory.getLog(AclEntryVoter.class);

    private AclService aclService;
    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();
    private SidRetrievalStrategy sidRetrievalStrategy = new RolesPublicSidRetrievalStrategy();
    private String internalMethod;
    private String processConfigAttribute;
    private List<Permission> requirePermission;

    public AclEntryVoter(AclService aclService, String processConfigAttribute, List<Permission> requirePermission) {
        Assert.notNull(processConfigAttribute, "A processConfigAttribute is mandatory");
        Assert.notNull(aclService, "An AclService is mandatory");

        if ((requirePermission == null) || (requirePermission.size() == 0)) {
            throw new IllegalArgumentException("One or more requirePermission entries is mandatory");
        }

        this.aclService = aclService;
        this.processConfigAttribute = processConfigAttribute;
        this.requirePermission = requirePermission;
    }

    /**
     * Optionally specifies a method of the domain object that will be used to obtain a contained domain
     * object. That contained domain object will be used for the ACL evaluation. This is useful if a domain object
     * contains a parent that an ACL evaluation should be targeted for, instead of the child domain object (which
     * perhaps is being created and as such does not yet have any ACL permissions)
     *
     * @return <code>null</code> to use the domain object, or the name of a method (that requires no arguments) that
     *         should be invoked to obtain an <code>Object</code> which will be the domain object used for ACL
     *         evaluation
     */
    protected String getInternalMethod() {
        return internalMethod;
    }

    public void setInternalMethod(String internalMethod) {
        this.internalMethod = internalMethod;
    }

    protected String getProcessConfigAttribute() {
        return processConfigAttribute;
    }

    public void setObjectIdentityRetrievalStrategy(ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        Assert.notNull(objectIdentityRetrievalStrategy, "ObjectIdentityRetrievalStrategy required");
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        Assert.notNull(sidRetrievalStrategy, "SidRetrievalStrategy required");
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    public boolean supports(ConfigAttribute attribute) {
        return ((attribute.getAttribute() != null) && attribute.getAttribute().equals(getProcessConfigAttribute()));
    }

    protected Object[] getDomainObjectInstances(Object secureObject) {
    	List<Object> result = new ArrayList<Object>();
    	
        Object[] args;
        Class<?>[] params;

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
            if (getProcessDomainObjectClass().isAssignableFrom(params[i])) {
            	result.add(args[i]);
            }
        }

        if (result.size() == 0) {
	        throw new AuthorizationServiceException("Secure object: " + secureObject
	            + " did not provide any argument of type: " + getProcessDomainObjectClass());
        }
        
        return result.toArray(new Object[0]);
    }

    public int vote(Authentication authentication, MethodInvocation object, Collection<ConfigAttribute> attributes) {
        for(ConfigAttribute attr : attributes) {
            if (!this.supports(attr)) {
                continue;
            }
            // Need to make an access decision on this invocation
            // Attempt to locate the domain object instance to process
            Object[] domainObjects = getDomainObjectInstances(object);

            // Obtain the SIDs applicable to the principal
            List<Sid> sids = sidRetrievalStrategy.getSids(authentication);

            boolean found = false;
            
            for (Object domainObject : domainObjects) {
	            // If domain object is null, vote to abstain
	            if (domainObject == null) {
	                continue;
	            }
	
	            found = true;
	            
	            // Evaluate if we are required to use an inner domain object
	            if (StringUtils.hasText(internalMethod)) {
	                try {
	                    Class<?> clazz = domainObject.getClass();
	                    Method method = clazz.getMethod(internalMethod, new Class[0]);
	                    domainObject = method.invoke(domainObject, new Object[0]);
	                } catch (NoSuchMethodException nsme) {
	                    throw new AuthorizationServiceException("Object of class '" + domainObject.getClass()
	                        + "' does not provide the requested internalMethod: " + internalMethod);
	                } catch (IllegalAccessException iae) {
	                    logger.debug("IllegalAccessException", iae);
	
	                    throw new AuthorizationServiceException("Problem invoking internalMethod: " + internalMethod
	                        + " for object: " + domainObject);
	                } catch (InvocationTargetException ite) {
	                    logger.debug("InvocationTargetException", ite);
	
	                    throw new AuthorizationServiceException("Problem invoking internalMethod: " + internalMethod
	                        + " for object: " + domainObject);
	                }
	            }
	
	            // Obtain the OID applicable to the domain object
	            ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
	
	            Acl acl;
	
	            try {
	                // Lookup only ACLs for SIDs we're interested in
	                acl = aclService.readAclById(objectIdentity, sids);
	            } catch (NotFoundException nfe) {
	                if (logger.isDebugEnabled()) {
	                    logger.debug("Voting to deny access - no ACLs apply for this principal");
	                }
	
	                return AccessDecisionVoter.ACCESS_DENIED;
	            }
	
	            try {
	                if (!acl.isGranted(requirePermission, sids, false)) {
	                    if (logger.isDebugEnabled()) {
	                        logger.debug(
	                            "Voting to deny access - ACLs returned, but insufficient permissions for this principal");
	                    }
	
	                    return AccessDecisionVoter.ACCESS_DENIED;
	                }
	            } catch (NotFoundException nfe) {
	                if (logger.isDebugEnabled()) {
	                    logger.debug("Voting to deny access - no ACLs apply for this principal");
	                }
	
	                return AccessDecisionVoter.ACCESS_DENIED;
	            }
            }

            if (found) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Voting to grant access");
                }
                return AccessDecisionVoter.ACCESS_GRANTED;
            }
        }

        // No configuration attribute matched, so abstain
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }



}
