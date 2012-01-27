package sk.seges.sesam.pap.service.model;

import java.util.Arrays;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ServiceConvertProviderType extends DelegateMutableDeclaredType {

	private final ServiceTypeElement serviceTypeElement;
	
	public static final String CONVERTER_PROVIDER_SUFFIX = "ConverterProvider";

	public ServiceConvertProviderType(ServiceTypeElement serviceTypeElement, MutableProcessingEnvironment processingEnv) {
		this.serviceTypeElement = serviceTypeElement;

		setKind(MutableTypeKind.CLASS);
		MutableDeclaredType converterProviderType = processingEnv.getTypeUtils().toMutableType(ConverterProvider.class);
		setInterfaces(Arrays.asList(converterProviderType));
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return serviceTypeElement.clone().addClassSufix(CONVERTER_PROVIDER_SUFFIX);
	}

}