package sk.seges.acris.security.server.spring.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.AffirmativeBased;
import org.springframework.security.vote.RoleVoter;
import org.springframework.security.vote.UnanimousBased;

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