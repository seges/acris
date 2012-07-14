package sk.seges.corpis.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class HibernateServiceConverterProviderPrinter extends HibernateConverterProviderPrinter {

	private final ServiceTypeElement serviceTypeElement;
	
	public HibernateServiceConverterProviderPrinter(FormattedPrintWriter pw,
			TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver, ServiceTypeElement serviceTypeElement) {
		super(pw, processingEnv, parametersResolver);
		this.serviceTypeElement = serviceTypeElement;
	}

	private static final String CONVERTER_PROVIDER_REFERENCE = "converterProvider";
	public static final String GET_CONVERTER_PROVIDER_METHOD = "getConverterProvider";
	
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