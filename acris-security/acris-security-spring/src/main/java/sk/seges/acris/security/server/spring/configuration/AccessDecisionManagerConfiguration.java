package sk.seges.acris.security.server.spring.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;

public class AccessDecisionManagerConfiguration {

	@Bean
    public RoleVoter roleVoter() {
    	return new RoleVoter();
    }


	@Bean
	public UnanimousBased unanonimousDecisionManager() {
		UnanimousBased decisionManager = new UnanimousBased();
		decisionManager.setAllowIfAllAbstainDecisions(true);

		List<AccessDecisionVoter> decissionVoters = new ArrayList<AccessDecisionVoter>();
		decissionVoters.add(roleVoter());
		decisionManager.setDecisionVoters(decissionVoters);

		return decisionManager;
	}

	@Bean
	public AffirmativeBased AffirmativeAccessDecisionManager() {
		AffirmativeBased decisionManager = new AffirmativeBased();
		decisionManager.setAllowIfAllAbstainDecisions(false);
		
		List<AccessDecisionVoter> decissionVoters = new ArrayList<AccessDecisionVoter>();
		decissionVoters.add(roleVoter());
		decisionManager.setDecisionVoters(decissionVoters);
		
		return decisionManager;
	}
}