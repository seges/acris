package sk.seges.corpis.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class HibernateServiceConverterProviderPrinter extends AbstractHibernateConverterProviderPrinter {

	private final ServiceTypeElement serviceTypeElement;
	
	public HibernateServiceConverterProviderPrinter(FormattedPrintWriter pw,
			TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver, ServiceTypeElement serviceTypeElement) {
		super(pw, processingEnv, parametersResolver);
		this.serviceTypeElement = serviceTypeElement;
	}

	private static final String CONVERTER_PROVIDER_REFERENCE = "converterProvider";
	
	@Override
	protected MutableReferenceType getConverterProviderReference() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		return typeUtils.getReference(typeUtils.getReferenceValue(new ServiceConvertProviderType(serviceTypeElement, processingEnv), 
				typeUtils.getReference(null, HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME)), CONVERTER_PROVIDER_REFERENCE);
	}
}