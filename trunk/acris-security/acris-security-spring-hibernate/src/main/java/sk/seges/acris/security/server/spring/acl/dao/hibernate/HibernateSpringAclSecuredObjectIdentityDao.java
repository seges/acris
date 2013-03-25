package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclSecuredObjectIdentityDao;

@Component
public class HibernateSpringAclSecuredObjectIdentityDao extends HibernateAclSecuredObjectIdentityDao {

	@Override
	@Transactional
    public JpaAclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity) {
		return super.findByObjectId(objectIdClass, objectIdIdentity);
    }

	@Override
	@Transactional
	public AclSecuredObjectIdentityData findById(long id) {
		return super.findById(id);
	}
	
	@Override
	public List<JpaAclSecuredObjectIdentity> findByParent(JpaAclSecuredObjectIdentity parentObjectIdentityId) {
		return super.findByParent(parentObjectIdentityId);
	}
}
