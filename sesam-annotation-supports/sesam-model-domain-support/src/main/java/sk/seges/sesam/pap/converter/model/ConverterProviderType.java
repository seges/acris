package sk.seges.sesam.pap.converter.model;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.pap.converter.util.HasConstructorParametersDelegate;
import sk.seges.sesam.pap.converter.util.ProjectNameResolver;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.provider.AbstractConverterProvider;

public class ConverterProviderType extends DelegateMutableDeclaredType implements HasConstructorParameters {

	protected final MutableDeclaredType mutableType;
	protected final MutableProcessingEnvironment processingEnv;
	
	public static final String CONVERTER_PROVIDER_SUFFIX = "ConverterProvider";

	public ConverterProviderType(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv) {
		this.mutableType = mutableType;
		this.processingEnv = processingEnv;

		setKind(MutableTypeKind.CLASS);
		setSuperClass(processingEnv.getTypeUtils().toMutableType(AbstractConverterProvider.class));
	}
	
	protected void setModifiers() {
		addModifier(Modifier.PUBLIC);
	}
	
	public ParameterElement[] getConverterParameters(ConverterConstructorParametersResolver parametersResolver) {
		return new HasConstructorParametersDelegate().getConverterParameters(processingEnv, parametersResolver);
	}
	
	protected ProjectNameResolver getNameResolver() {
		return new ProjectNameResolver(processingEnv);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		
		DefaultPackageValidator packageValidator = new DefaultPackageValidatorProvider().get(mutableType.getPackageName());

		MutableDeclaredType result = mutableType.clone().setSimpleName(getNameResolver().getName() + CONVERTER_PROVIDER_SUFFIX);
		
		if (packageValidator.isValid()) {
			result.changePackage(packageValidator.getGroup() + "." + packageValidator.getArtifact() + "." + packageValidator.getLocationType().getName());
		}
		
		return result;
	}
}