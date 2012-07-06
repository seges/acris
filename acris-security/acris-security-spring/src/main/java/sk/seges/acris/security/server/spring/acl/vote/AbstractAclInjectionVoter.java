package sk.seges.acris.security.server.spring.acl.vote;

import java.util.Iterator;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.util.Assert;

import sk.seges.acris.security.server.spring.acl.sid.RolesPublicSidRetrievalStrategy;

public abstract class AbstractAclInjectionVoter implements AccessDecisionVoter {

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

	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {

		int result = ACCESS_ABSTAIN;

		Iterator<?> iter = config.getConfigAttributes().iterator();

		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();

			if (this.supports(attribute)) {

				Object[] args;
				Class<?>[] params;

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

				injectIntoCriteria(sids, params, args);

				return result;
			}
		}

		return result;
	}

	protected void injectIntoCriteria(Sid[] sids, Class<?>[] params, Object[] args) {
	}
}
