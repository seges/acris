package sk.seges.sesam.pap.service.model;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.converter.model.HasConstructorParameters;
import sk.seges.sesam.pap.converter.util.HasConstructorParametersDelegate;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

public class ConverterProviderContextType extends DelegateMutableDeclaredType implements HasConstructorParameters {

	protected final MutableDeclaredType type;
	protected final MutableProcessingEnvironment processingEnv;

	public static final String CONVERTER_PROVIDER_CONTEXT_SUFFIX = "ConverterProviderContext";

	public ConverterProviderContextType(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		this.type = type;
		this.processingEnv = processingEnv;

		addModifier(Modifier.PUBLIC);
		setKind(MutableTypeKind.CLASS);
		setSuperClass(processingEnv.getTypeUtils().toMutableType(getContextSuperClass()));
	}

    protected Class<?> getContextSuperClass() {
        return ConverterProviderContext.class;
    }

    @Override
	protected MutableDeclaredType getDelegate() {
		return type.clone().setSimpleName(type.getSimpleName() + CONVERTER_PROVIDER_CONTEXT_SUFFIX);
	}

	@Override
	public ParameterElement[] getConverterParameters(ConverterConstructorParametersResolver parametersResolver) {
		return new HasConstructorParametersDelegate().getConverterParameters(processingEnv, parametersResolver);
	}	
}