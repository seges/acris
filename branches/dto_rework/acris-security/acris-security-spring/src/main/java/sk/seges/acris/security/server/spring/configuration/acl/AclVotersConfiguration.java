package sk.seges.acris.security.server.spring.configuration.acl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationCollectionFilteringProvider;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationProvider;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.AclEntryVoter;
import org.springframework.security.vote.RoleVoter;
import org.springframework.security.vote.UnanimousBased;

import sk.seges.acris.security.server.spring.acl.vote.VoterPermissions;
import sk.seges.acris.security.shared.domain.ISecuredObject;

public class AclVotersConfiguration {

	@Autowired
	private AclService aclService;

	@Autowired
	private RoleVoter roleVoter;
	
	@Bean
	public DefaultPermissionFactory permissionFactory() {
		return new DefaultPermissionFactory();
	}

	@Bean
	public VoterPermissions voterPermissions() {
		VoterPermissions voterPermissions = new VoterPermissions();
		voterPermissions.setPermissionFactory(permissionFactory());
		voterPermissions.init();
		return voterPermissions;
	}

	@Bean
	public AclEntryVoter aclObjectReadVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_OBJECT_VIEW", voterPermissions().READ);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclEntryListReadVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_LIST_OBJECTS_VIEW", voterPermissions().READ);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclObjectWriteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_OBJECT_EDIT", voterPermissions().WRITE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclEntryWriteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_OBJECTS_EDIT", voterPermissions().WRITE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclEntryListWriteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_LIST_OBJECTS_EDIT", voterPermissions().WRITE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclObjectDeleteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_OBJECT_DELETE", voterPermissions().DELETE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclEntryDeleteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_OBJECTS_DELETE", voterPermissions().DELETE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryVoter aclEntryListDeleteVoter() {
		AclEntryVoter voter = new AclEntryVoter(aclService, "ACL_LIST_OBJECTS_DELETE", voterPermissions().DELETE);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		return voter;
	}

	@Bean
	public AclEntryAfterInvocationCollectionFilteringProvider afterAclCollectionRead() {
		return new AclEntryAfterInvocationCollectionFilteringProvider(aclService, voterPermissions().READ);
	}

	@Bean
	public AclEntryAfterInvocationProvider afterAclRead() {
		return new AclEntryAfterInvocationProvider(aclService, voterPermissions().READ);
	}
	
	@Bean
	public UnanimousBased businessAccessDecisionManager() {
		UnanimousBased voter = new UnanimousBased();
		voter.setAllowIfAllAbstainDecisions(true);
		
		List<AccessDecisionVoter> decissionVoters = new ArrayList<AccessDecisionVoter>();

		decissionVoters.add(roleVoter);
		decissionVoters.add(aclObjectReadVoter());
		decissionVoters.add(aclObjectWriteVoter());
		decissionVoters.add(aclObjectDeleteVoter());
		decissionVoters.add(aclEntryListReadVoter());
		decissionVoters.add(aclEntryListWriteVoter());
		decissionVoters.add(aclEntryListDeleteVoter());

		return voter;
	}

}