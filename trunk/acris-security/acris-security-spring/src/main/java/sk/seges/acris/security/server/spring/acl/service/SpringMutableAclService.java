package sk.seges.acris.security.server.spring.acl.service;

import java.util.List;

import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AlreadyExistsException;
import org.springframework.security.acls.ChildrenExistException;
import org.springframework.security.acls.MutableAcl;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;
import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.spring.user_management.domain.SpringUserAdapter;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringMutableAclService extends SpringAclService implements MutableAclService {

	public SpringMutableAclService(AclCache aclCache) {
		super(aclCache);
	}
	
	@Transactional
	public MutableAcl createAcl(ObjectIdentity objectIdentity) throws AlreadyExistsException {

		AclSecuredObjectIdentity aclObjectIdentity = getAclSecuredObjectIdentity(objectIdentity);
		// Check this object identity hasn't already been persisted
		if (aclObjectIdentity != null) {
			throw new AlreadyExistsException("Object identity '" + aclObjectIdentity + "' already exists");
		}

		AclSecuredClassDescription aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getJavaType());

		// Need to retrieve the current principal, in order to know who "owns"
		// this ACL (can be changed later on)
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PrincipalSid sid = new PrincipalSid(auth);

		String sidName = getSidName(sid);
		boolean principal = isPrincipal(sid);

		AclSid aclSid = aclSecurityIDDao.loadOrCreate(sidName, principal);

		aclObjectIdentity = aclObjectIdentityDao.createDefaultEntity();
		aclObjectIdentity.setObjectIdClass(aclClass);
		aclObjectIdentity.setSid(aclSid);

		aclObjectIdentity.setObjectIdIdentity(Long.parseLong((objectIdentity.getIdentifier().toString())));
		aclObjectIdentity.setEntriesInheriting(true);
		aclObjectIdentityDao.persist(aclObjectIdentity);

		// Retrieve the ACL via superclass (ensures cache registration, proper
		// retrieval etc)
		Acl acl = readAclById(objectIdentity);
		return (MutableAcl) acl;
	}

	private String getSidName(Sid sid) {
		if (sid instanceof PrincipalSid) {
			return ((PrincipalSid) sid).getPrincipal();
		} else if (sid instanceof GrantedAuthoritySid) {
			return ((GrantedAuthoritySid) sid).getGrantedAuthority();
		} else {
			throw new IllegalArgumentException("Unsupported implementation of Sid");
		}
	}

	private boolean isPrincipal(Sid sid) {
		if (sid instanceof PrincipalSid) {
			return true;
		} else if (sid instanceof GrantedAuthoritySid) {
			return false;
		} else {
			throw new IllegalArgumentException("Unsupported implementation of Sid");
		}
	}

	protected void createRecords(final MutableAcl acl) {
		int i = 1;
		for (AccessControlEntry entry_ : acl.getEntries()) {
			AccessControlEntryImpl entry = (AccessControlEntryImpl) entry_;
			AclEntry aclEntry = aclEntryDao.createDefaultEntity();
			long oid = ((Long) acl.getId()).longValue();
			aclEntry.setObjectIdentity(aclObjectIdentityDao.findById(oid));
			aclEntry.setAceOrder(i);
			Sid sid = entry.getSid();

			String sidName = getSidName(sid);
			boolean principal = isPrincipal(sid);

			AclSid aclSid = aclSecurityIDDao.loadOrCreate(sidName, principal);
			aclEntry.setSid(aclSid);
			aclEntry.setAuditFailure(entry.isAuditFailure());
			aclEntry.setAuditSuccess(entry.isAuditSuccess());
			aclEntry.setGranting(entry.isGranting());
			aclEntry.setMask(entry.getPermission().getMask());
			aclEntryDao.persist(aclEntry);
			i++;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.security.acls.MutableAclService#deleteAcl(org.springframework.security.acls.objectidentity
	 * .ObjectIdentity, boolean)
	 */
	@Transactional
	public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren) throws ChildrenExistException {
		AclSecuredClassDescription aclClass = aclSecuredClassDao.load(objectIdentity.getJavaType());
		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentity aclObjectIdentity = aclObjectIdentityDao.findByObjectId(aclClass.getId(), id);
		if (aclObjectIdentity != null) {
			removeAcl(aclObjectIdentity);
			aclCache.evictFromCache(objectIdentity);
		}
	}
	
	private void removeAcl(AclSecuredObjectIdentity aclObjectIdentity) {
		List<AclSecuredObjectIdentity> referencedObjectIdentities = aclObjectIdentityDao.findByParent(aclObjectIdentity);
		if (referencedObjectIdentities != null || !referencedObjectIdentities.isEmpty()) {
			for (AclSecuredObjectIdentity objectIdentity : referencedObjectIdentities) {
				removeAcl(objectIdentity);
			}
		}
		aclEntryDao.deleteByIdentityId(aclObjectIdentity.getId());
		aclObjectIdentityDao.remove(aclObjectIdentity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.acls.MutableAclService#updateAcl(org.springframework.security.acls.MutableAcl)
	 */
	@Transactional
	public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
		// Delete this ACL's ACEs in the acl_entry table
		// long oid = ((Long) acl.getId()).longValue();
		AclSecuredObjectIdentity aclo = updateAclObjectIdentity(acl);
		
		aclEntryDao.deleteByIdentityId(aclo.getId());
		aclCache.evictFromCache(acl.getObjectIdentity());
		// Create this ACL's ACEs in the acl_entry table
		createRecords(acl);

		// Retrieve the ACL via superclass (ensures cache registration, proper
		// retrieval etc)
		return (MutableAcl) readAclById(acl.getObjectIdentity());
	}
		
	private AclSecuredObjectIdentity updateAclObjectIdentity(MutableAcl acl) {
		AclSecuredObjectIdentity aclo = getAclSecuredObjectIdentity(acl.getObjectIdentity());
		
		if (acl.getParentAcl() != null) {
			AclSecuredObjectIdentity parentObjectIdentity = getAclSecuredObjectIdentity(acl.getParentAcl().getObjectIdentity());
			aclo.setParentObject(parentObjectIdentity);
		} else if (aclo.getParentObject() != null) {
			aclo.setParentObject(null);
		}
		aclObjectIdentityDao.merge(aclo);

		return aclo;
	}

	@Transactional
	public AclSecuredObjectIdentity getAclSecuredObjectIdentity(ObjectIdentity objectIdentity) {
		AclSecuredClassDescription aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getJavaType());

		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentity aclObjectIdentity = aclObjectIdentityDao.findByObjectId(aclClass.getId(), id);
		return aclObjectIdentity;

	}

	public void addPermission(ISecuredObject secureObject, Permission permission, Class<?> clazz) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Sid recipient;
		if (auth.getPrincipal() instanceof UserDetails) {
			recipient = new PrincipalSid(((UserDetails) auth.getPrincipal()).getUsername());
		} else if (auth.getPrincipal() instanceof UserData) {
			recipient = new PrincipalSid(new SpringUserAdapter((UserData)auth.getPrincipal()).getUsername());
		} else {
			recipient = new PrincipalSid(auth.getPrincipal().toString());
		}

		addPermission(secureObject, recipient, permission, clazz);
	}

	public void addPermission(ISecuredObject securedObject, Sid recipient, Permission permission, Class<?> clazz) {
		MutableAcl acl;

		ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getIdForACL());

		try {
			acl = (MutableAcl) readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = createAcl(oid);
		}

		acl.insertAce(acl.getEntries().length, permission, recipient, true);
		updateAcl(acl);
	}
}