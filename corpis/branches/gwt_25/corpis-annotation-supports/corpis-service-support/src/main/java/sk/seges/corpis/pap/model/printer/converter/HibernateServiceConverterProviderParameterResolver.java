package sk.seges.corpis.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateConverterProviderParameterResolver;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class HibernateServiceConverterProviderParameterResolver extends HibernateConverterProviderParameterResolver {

	private final ServiceTypeElement serviceTypeElement;

	private static final String CONVERTER_PROVIDER_REFERENCE = "converterProvider";
	public static final String GET_CONVERTER_PROVIDER_METHOD = "getConverterProvider";

	public HibernateServiceConverterProviderParameterResolver(MutableProcessingEnvironment processingEnv, ServiceTypeElement serviceTypeElement) {
		super(processingEnv);
		this.serviceTypeElement = serviceTypeElement;
	}

	@Override
	protected MutableReferenceType getConverterProviderReference() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableExecutableElement converterProviderMethod = processingEnv.getElementUtils().getExecutableElement(GET_CONVERTER_PROVIDER_METHOD);
		converterProviderMethod.asType().setReturnType(new ServiceConvertProviderType(serviceTypeElement, processingEnv));
		
		return typeUtils.getReference(
					typeUtils.getReferenceToMethod(converterProviderMethod,
							typeUtils.getReference(null, HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME)), 
				CONVERTER_PROVIDER_REFERENCE);
	}
}