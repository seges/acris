package sk.seges.acris.security.server.acl.vote;

import java.util.Iterator;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.DetachedCriteriaUtils;
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

import sk.seges.acris.security.server.acl.domain.AclSid;
import sk.seges.acris.security.server.acl.domain.AclEntry;
import sk.seges.acris.security.server.acl.domain.AclSecuredClassDescription;
import sk.seges.acris.security.server.acl.domain.AclSecuredObjectIdentity;

public class PostAclInjectionVoter implements AccessDecisionVoter {

    private final String configAttribute;

    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

    public PostAclInjectionVoter(String configAttribute, Permission[] requirePermission) {
        this.configAttribute = configAttribute;
    }

    public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null) && attribute.getAttribute().equals(configAttribute)) {
            return true;
        } else {
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

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {

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
                        Class<?> entityClazz = new DetachedCriteriaUtils().getDetachedCriteriaDomainObjectClass(((DetachedCriteria) args[index]));
                        createCriteria(((DetachedCriteria) args[index]), sids, entityClazz);
                    }

                    index++;
                }

                return result;
            }
        }

        return result;
    }

    protected DetachedCriteria createCriteria(DetachedCriteria clazzCriteria, Sid[] sids, Class<?> clazz) {

        // Create detached criteria with alias - name of the alias is not
        // importat, only purpose
        // is to not use this_ default alias
        DetachedCriteria criteria = DetachedCriteria.forClass(AclEntry.class, "aclEntry");

        // We just want to select objectIdentities
        criteria.setProjection(Projections.alias(Projections.property(AclEntry.A_OBJECT_IDENTITY), "object_identity"));

        criteria.createCriteria(AclEntry.A_OBJECT_IDENTITY).
        // select secured object id
                add(Restrictions.sqlRestriction("{alias}." + AclSecuredObjectIdentity.DB_OBJECT_IDENTITY_ID + "=this_.id")).createCriteria(
                        AclSecuredObjectIdentity.A_OBJECT_CLASS).
                // select secured object class
                add(Restrictions.eq(AclSecuredClassDescription.CLASS_NAME_FIELD, clazz.getName()));

        // create disjunction of the principals
        Junction junction = Expression.disjunction();

        for (Sid sid : sids) {
            if (sid instanceof PrincipalSid) {
                junction.add(Restrictions.eq(AclSid.A_SID, ((PrincipalSid) sid).getPrincipal()));
            }
        }

        criteria.createCriteria(AclEntry.A_SID).add(junction);

        // combine sub-queries
        clazzCriteria.add(Subqueries.exists(criteria));

        return clazzCriteria;
    }

}
