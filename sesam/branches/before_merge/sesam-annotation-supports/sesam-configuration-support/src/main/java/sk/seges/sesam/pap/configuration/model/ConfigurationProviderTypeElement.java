package sk.seges.sesam.pap.configuration.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor.RoundContext;

public class ConfigurationProviderTypeElement extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Provider";

	private RoundContext roundContext;
	
	public ConfigurationProviderTypeElement(RoundContext roundContext) {
		this.roundContext = roundContext;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType result = roundContext.getMutableType().clone();
		result.addClassSufix(SUFFIX);
		result.setKind(MutableTypeKind.INTERFACE);
		return result;
	}
}