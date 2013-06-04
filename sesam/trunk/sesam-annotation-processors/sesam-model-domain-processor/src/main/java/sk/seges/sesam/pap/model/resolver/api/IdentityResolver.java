package sk.seges.sesam.pap.model.resolver.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface IdentityResolver {

	//TODO Use ConfigurationTypeElement
	boolean shouldHaveIdMethod(ConfigurationTypeElement configurationElement, TypeMirror domainType);
}
