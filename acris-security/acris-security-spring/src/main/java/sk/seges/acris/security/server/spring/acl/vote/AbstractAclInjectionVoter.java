package sk.seges.acris.security.server.spring.acl.vote;

import java.util.Collection;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import sk.seges.acris.security.server.spring.acl.sid.RolesPublicSidRetrievalStrategy;

public class AbstractAclInjectionVoter implements AccessDecisionVoter {

	private final String configAttribute;

	private SidRetrievalStrategy sidRetrievalStrategy = new RolesPublicSidRetrievalStrategy();

	public AbstractAclInjectionVoter(String configAttribute, Permission[] requirePermission) {
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

	public int vote(Authentication authentication, Object object, Collection attributes) {
		int result = ACCESS_ABSTAIN;

		for (ConfigAttribute attribute : (Collection<ConfigAttribute>)attributes) {
			if (this.supports(attribute)) {

				Object[] args;
				Class<?>[] params;

				if (object instanceof MethodInvocation) {
					MethodInvocation invocation = (MethodInvocation) object;
					params = invocation.getMethod().getParameterTypes();
					args = invocation.getArguments();
				} else {
					JoinPoint jp = (JoinPoint) object;
					params = ((CodeSignature) jp.getStaticPart().getSignature())
							.getParameterTypes();
					args = jp.getArgs();
				}

				List<Sid> sids = sidRetrievalStrategy.getSids(authentication);

				injectIntoCriteria(sids, params, args);

				return result;
			}
		}

		return result;
	}

	protected void injectIntoCriteria(List<Sid> sids, Class<?>[] params, Object[] args) {
	}

}
