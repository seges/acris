package sk.seges.acris.security.server.spring.acl.vote.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.DetachedCriteriaUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclEntry;
import sk.seges.acris.security.server.spring.acl.vote.AbstractAclInjectionVoter;


public class HibernateAclInjectorVoter extends AbstractAclInjectionVoter {

    public HibernateAclInjectorVoter(String configAttribute, Permission[] requirePermission) {
		super(configAttribute, requirePermission);
	}

	protected DetachedCriteria createCriteria(DetachedCriteria clazzCriteria, List<Sid> sids, Class<?> clazz) {

        // Create detached criteria with alias - name of the alias is not
        // importat, only purpose
        // is to not use this_ default alias
        DetachedCriteria criteria = DetachedCriteria.forClass(JpaAclEntry.class, "aclEntry");

        // We just want to select objectIdentities
        criteria.setProjection(Projections.alias(Projections.property(AclEntryData.OBJECT_IDENTITY), "object_identity"));

        criteria.createCriteria(AclEntryData.OBJECT_IDENTITY).
        // select secured object id
                add(Restrictions.sqlRestriction("{alias}.object_id_identity=this_.id")).createCriteria(
                        AclSecuredObjectIdentityData.OBJECT_ID_CLASS).
                // select secured object class
                add(Restrictions.eq(AclSecuredClassDescriptionData.CLASS_NAME, clazz.getName()));

        // create disjunction of the principals
        Junction junction = Restrictions.disjunction();

        for (Sid sid : sids) {
            if (sid instanceof PrincipalSid) {
                junction.add(Restrictions.eq(AclSidData.SID, ((PrincipalSid) sid).getPrincipal()));
            }
        }

        criteria.createCriteria(AclEntryData.SID).add(junction);

        // combine sub-queries
        clazzCriteria.add(Subqueries.exists(criteria));

        return clazzCriteria;
    }
    
	@Override
    protected void injectIntoCriteria(List<Sid> sids, Class<?>[] params, Object[] args) {
        int index = 0;
        for (Class<?> clazz : params) {
            if (clazz.isAssignableFrom(DetachedCriteria.class)) {
                Class<?> entityClazz = new DetachedCriteriaUtils().getDetachedCriteriaDomainObjectClass(((DetachedCriteria) args[index]));
                createCriteria(((DetachedCriteria) args[index]), sids, entityClazz);
            }

            index++;
        }
    }
}
