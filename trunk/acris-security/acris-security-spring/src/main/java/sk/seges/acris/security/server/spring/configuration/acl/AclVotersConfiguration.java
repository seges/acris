package sk.seges.acris.security.server.spring.configuration.acl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationCollectionFilteringProvider;
import org.springframework.security.afterinvocation.AclEntryAfterInvocationProvider;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.AclEntryVoter;
import org.springframework.security.vote.RoleVoter;
import org.springframework.security.vote.UnanimousBased;

import sk.seges.acris.security.server.spring.acl.provider.BetterAclEntryAfterInvocationCollectionFilteringProvider;
import sk.seges.acris.security.server.spring.acl.sid.RolesPublicSidRetrievalStrategy;
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
		return getVoter(aclService, "ACL_OBJECT_VIEW", voterPermissions().READ);
	}

	@Bean
	public AclEntryVoter aclEntryListReadVoter() {
		return getVoter(aclService, "ACL_LIST_OBJECTS_VIEW", voterPermissions().READ);
	}

	@Bean
	public AclEntryVoter aclObjectWriteVoter() {
		return getVoter(aclService, "ACL_OBJECT_EDIT", voterPermissions().WRITE);
	}

	@Bean
	public AclEntryVoter aclEntryWriteVoter() {
		return getVoter(aclService, "ACL_OBJECTS_EDIT", voterPermissions().WRITE);
	}

	@Bean
	public AclEntryVoter aclEntryListWriteVoter() {
		return getVoter(aclService, "ACL_LIST_OBJECTS_EDIT", voterPermissions().WRITE);
	}

	@Bean
	public AclEntryVoter aclObjectDeleteVoter() {
		return getVoter(aclService, "ACL_OBJECT_DELETE", voterPermissions().DELETE);
	}

	@Bean
	public AclEntryVoter aclEntryDeleteVoter() {
		return getVoter(aclService, "ACL_OBJECTS_DELETE", voterPermissions().DELETE);
	}

	@Bean
	public AclEntryVoter aclEntryListDeleteVoter() {
		return getVoter(aclService, "ACL_LIST_OBJECTS_DELETE", voterPermissions().DELETE);
	}

	@Bean
	public AclEntryAfterInvocationCollectionFilteringProvider afterAclCollectionRead(SidRetrievalStrategy sidRetrievalStrategy) {
		BetterAclEntryAfterInvocationCollectionFilteringProvider provider = new BetterAclEntryAfterInvocationCollectionFilteringProvider(aclService, voterPermissions().READ);
		provider.setSidRetrievalStrategy(sidRetrievalStrategy);
		return provider;
	}

	@Bean
	public AclEntryAfterInvocationProvider afterAclRead(SidRetrievalStrategy sidRetrievalStrategy) {
		AclEntryAfterInvocationProvider provider = new AclEntryAfterInvocationProvider(aclService, voterPermissions().READ);
		provider.setSidRetrievalStrategy(sidRetrievalStrategy);
		return provider;
	}
	
	@Bean
	public SidRetrievalStrategy sidRetrievalStrategy() {
		return new RolesPublicSidRetrievalStrategy();
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
	
	private AclEntryVoter getVoter(AclService aclService, String processConfigAttribute, Permission[] requirePermission) {
		AclEntryVoter voter = new AclEntryVoter(aclService, processConfigAttribute, requirePermission);
		voter.setProcessDomainObjectClass(ISecuredObject.class);
		voter.setSidRetrievalStrategy(sidRetrievalStrategy());
		return voter;
	}

}