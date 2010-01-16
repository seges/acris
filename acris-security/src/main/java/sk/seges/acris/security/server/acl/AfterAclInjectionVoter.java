package sk.seges.acris.security.server.acl;

import java.util.Iterator;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.DetachedCriteriaProcessor;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.acls.sid.SidRetrievalStrategyImpl;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.util.Assert;

import sk.seges.acris.security.server.domain.acl.ACLEntry;
import sk.seges.acris.security.server.domain.acl.ACLObjectIdentity;
import sk.seges.acris.security.server.domain.acl.ACLSecuredClass;
import sk.seges.acris.security.server.domain.acl.ACLSecurityID;

public class AfterAclInjectionVoter implements AccessDecisionVoter {

	private final String configAttribute;
	
    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	public AfterAclInjectionVoter(String configAttribute, Permission[] requirePermission) {
		this.configAttribute = configAttribute;
	}
	
	public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null) && attribute.getAttribute().equals(configAttribute)) {
            return true;
        }
        else {
            return false;
        }
	}

	public boolean supports(Class clazz) {
        return true;
	}

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        Assert.notNull(sidRetrievalStrategy, "SidRetrievalStrategy required");
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

	public int vote(Authentication authentication, Object object,
			ConfigAttributeDefinition config) {

        int result = ACCESS_ABSTAIN;

        Iterator iter = config.getConfigAttributes().iterator();

        while (iter.hasNext()) {
            ConfigAttribute attribute = (ConfigAttribute) iter.next();

            if (this.supports(attribute)) {

		        Object[] args;
		        Class[] params;
		
		        if (object instanceof MethodInvocation) {
		            MethodInvocation invocation = (MethodInvocation) object;
		            params = invocation.getMethod().getParameterTypes();
		            args = invocation.getArguments();
		        } else {
		            JoinPoint jp = (JoinPoint) object;
		            params = ((CodeSignature) jp.getStaticPart().getSignature()).getParameterTypes();
		            args = jp.getArgs();
		        }
		        
		
		        Sid[] sids = sidRetrievalStrategy.getSids(authentication);
		        
		        int index = 0;
		        for (Class<?> clazz : params) {
		        	if (clazz.isAssignableFrom(DetachedCriteria.class)) {
		        		Class<?> entityClazz = new DetachedCriteriaProcessor().getEntityClassForDetachedCriteria(((DetachedCriteria)args[index]));
		        		createCriteria(((DetachedCriteria)args[index]), sids, entityClazz);
		        	}
		        	
		        	index++;
		        }
		        
		        return result;
            }
        }
        
		return result;
	}
	
	protected DetachedCriteria createCriteria(DetachedCriteria clazzCriteria, Sid[] sids, Class<?> clazz) {

		//Create detached criteria with alias - name of the alias is not importat, only purpose
		//is to not use this_ default alias
		DetachedCriteria criteria = DetachedCriteria.forClass(ACLEntry.class, "aclEntry");
		
		//We just want to select objectIdentities
		criteria.setProjection( Projections.alias( Projections.property(ACLEntry.OBJECT_IDENTITY_FIELD), "object_identity" ));

		criteria.createCriteria(ACLEntry.OBJECT_IDENTITY_FIELD).
		//select secured object id
				add(Restrictions.sqlRestriction("{alias}." + ACLObjectIdentity.OBJECT_IDENTITY_ID_DB_FIELD + "=this_.id")).
					createCriteria(ACLObjectIdentity.OBJECT_CLASS_FIELD).
		//select secured object class
						add(Restrictions.eq(ACLSecuredClass.CLASS_NAME_FIELD, clazz.getName()));
		
		//create disjunction of the principals
		Junction junction = Expression.disjunction();

		criteria.createCriteria(ACLEntry.SID_FIELD).add(junction);
		
		for (Sid sid : sids) {
			if (sid instanceof PrincipalSid) {
				junction.add(Restrictions.eq(ACLSecurityID.SID_FIELD, ((PrincipalSid)sid).getPrincipal()));
			}
		}
		
		//combine sub-queries
		clazzCriteria.add(
				Subqueries.exists(criteria)
		); 
		
		return clazzCriteria;
	}

}
