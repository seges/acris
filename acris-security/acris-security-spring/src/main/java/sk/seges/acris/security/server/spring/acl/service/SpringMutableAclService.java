package sk.seges.acris.security.server.spring.acl.service;

import java.util.List;

import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.ChildrenExistException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.sesam.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.spring.user_management.domain.SpringUserAdapter;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public class SpringMutableAclService extends SpringAclService implements MutableAclService {

	public SpringMutableAclService(AclCache aclCache) {
		super(aclCache);
	}
	
	@Transactional
	public MutableAcl createAcl(ObjectIdentity objectIdentity) throws AlreadyExistsException {

		AclSecuredObjectIdentityData aclObjectIdentity = getAclSecuredObjectIdentity(objectIdentity);
		// Check this object identity hasn't already been persisted
		if (aclObjectIdentity != null) {
			throw new AlreadyExistsException("Object identity '" + aclObjectIdentity + "' already exists");
		}

		AclSecuredClassDescriptionData aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getType());

		// Need to retrieve the current principal, in order to know who "owns"
		// this ACL (can be changed later on)
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PrincipalSid sid = new PrincipalSid(auth);

		String sidName = getSidName(sid);
		boolean principal = isPrincipal(sid);

		AclSidData aclSid = aclSecurityIDDao.loadOrCreate(sidName, principal);

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
			AclEntryData aclEntry = aclEntryDao.createDefaultEntity();
			long oid = ((Long) acl.getId()).longValue();
			aclEntry.setObjectIdentity(aclObjectIdentityDao.findById(oid));
			aclEntry.setAceOrder(i);
			Sid sid = entry.getSid();

			String sidName = getSidName(sid);
			boolean principal = isPrincipal(sid);

			AclSidData aclSid = aclSecurityIDDao.loadOrCreate(sidName, principal);
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
		AclSecuredClassDescriptionData aclClass = aclSecuredClassDao.load(objectIdentity.getType());
		if (aclClass == null) {
			return;
		}
		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentityData aclObjectIdentity = aclObjectIdentityDao.findByObjectId(aclClass.getId(), id);
		if (aclObjectIdentity != null) {
			removeAcl(aclObjectIdentity);
			aclCache.evictFromCache(objectIdentity);
		}
	}
	
	private void removeAcl(AclSecuredObjectIdentityData aclObjectIdentity) {
		List<AclSecuredObjectIdentityData> referencedObjectIdentities = aclObjectIdentityDao.findByParent(aclObjectIdentity);
		if (referencedObjectIdentities != null && !referencedObjectIdentities.isEmpty()) {
			for (AclSecuredObjectIdentityData objectIdentity : referencedObjectIdentities) {
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
		AclSecuredObjectIdentityData aclo = updateAclObjectIdentity(acl);
		
		aclEntryDao.deleteByIdentityId(aclo.getId());
		aclCache.evictFromCache(acl.getObjectIdentity());
		// Create this ACL's ACEs in the acl_entry table
		createRecords(acl);

		// Retrieve the ACL via superclass (ensures cache registration, proper
		// retrieval etc)
		return (MutableAcl) readAclById(acl.getObjectIdentity());
	}
		
	private AclSecuredObjectIdentityData updateAclObjectIdentity(MutableAcl acl) {
		AclSecuredObjectIdentityData aclo = getAclSecuredObjectIdentity(acl.getObjectIdentity());
		
		if (acl.getParentAcl() != null) {
			AclSecuredObjectIdentityData parentObjectIdentity = getAclSecuredObjectIdentity(acl.getParentAcl().getObjectIdentity());
			aclo.setParentObject(parentObjectIdentity);
		} else if (aclo.getParentObject() != null) {
			aclo.setParentObject(null);
		}
		aclObjectIdentityDao.merge(aclo);

		return aclo;
	}

	@Transactional
	public AclSecuredObjectIdentityData getAclSecuredObjectIdentity(ObjectIdentity objectIdentity) {
		AclSecuredClassDescriptionData aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getType());

		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentityData aclObjectIdentity = aclObjectIdentityDao.findByObjectId(aclClass.getId(), id);
		return aclObjectIdentity;

	}

	public void addPermission(ISecuredObject<?> secureObject, Permission permission, Class<?> clazz) {
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

	public void addPermission(ISecuredObject<?> securedObject, Sid recipient, Permission permission, Class<?> clazz) {
		MutableAcl acl;

		ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getIdForACL());

		try {
			acl = (MutableAcl) readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = createAcl(oid);
		}

		acl.insertAce(acl.getEntries().size(), permission, recipient, true);
		updateAcl(acl);
	}
}