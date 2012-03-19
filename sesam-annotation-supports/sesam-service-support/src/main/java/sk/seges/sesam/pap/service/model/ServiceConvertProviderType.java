package sk.seges.sesam.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ParametersFilter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.AbstractConverterProvider;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ServiceConvertProviderType extends DelegateMutableDeclaredType {

	private final ServiceTypeElement serviceTypeElement;
	private final MutableProcessingEnvironment processingEnv;
	
	public static final String CONVERTER_PROVIDER_SUFFIX = "ConverterProvider";

	public ServiceConvertProviderType(ServiceTypeElement serviceTypeElement, MutableProcessingEnvironment processingEnv) {
		this.serviceTypeElement = serviceTypeElement;
		this.processingEnv = processingEnv;

		setKind(MutableTypeKind.CLASS);
		setSuperClass(processingEnv.getTypeUtils().toMutableType(AbstractConverterProvider.class));
	}
	
	public ParameterElement[] getConverterParameters(ConverterConstructorParametersResolver parametersResolver) {
		
		MutableDeclaredType converterProviderType = processingEnv.getTypeUtils().toMutableType(ConverterProvider.class);

		ParameterElement[] generatedParameters = ParametersFilter.NOT_PROPAGATED.filterParameters(parametersResolver.getConstructorAditionalParameters());

		List<ParameterElement> filteredParameters = new ArrayList<ParameterElement>();
		for (ParameterElement generatedParameter: generatedParameters) {
			if (!generatedParameter.getType().equals(converterProviderType)) {
				filteredParameters.add(generatedParameter);
			}
		}
		
		return filteredParameters.toArray(new ParameterElement[] {});
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return serviceTypeElement.clone().addClassSufix(CONVERTER_PROVIDER_SUFFIX);
	}

}