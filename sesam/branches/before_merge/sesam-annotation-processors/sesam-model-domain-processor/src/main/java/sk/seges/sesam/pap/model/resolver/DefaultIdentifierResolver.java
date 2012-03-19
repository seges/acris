package sk.seges.sesam.pap.model.resolver;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.resolver.api.IdentityResolver;

public class DefaultIdentifierResolver implements IdentityResolver {

	@Override
	public boolean shouldHaveIdMethod(ConfigurationTypeElement configurationElement, TypeMirror domainType) {
		return false;
	}
}