package sk.seges.corpis.pap.model.printer.converter;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.converter.util.HasConstructorParametersDelegate;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateConverterProviderParameterResolver;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class HibernateServiceConverterProviderParameterResolver extends HibernateConverterProviderParameterResolver {

	private final ServiceTypeElement serviceTypeElement;
	private final ConverterConstructorParametersResolverProvider parametersResolverProvider;
	
	private static final String CONVERTER_PROVIDER_CONTEXT_REFERENCE = "converterProviderContext";
	public static final String GET_CONVERTER_PROVIDER_CONTEXT_METHOD = "getConverterProviderContext";

	public HibernateServiceConverterProviderParameterResolver(ConverterConstructorParametersResolverProvider parametersResolverProvider, MutableProcessingEnvironment processingEnv, ServiceTypeElement serviceTypeElement) {
		super(processingEnv);
		this.parametersResolverProvider = parametersResolverProvider;
		this.serviceTypeElement = serviceTypeElement;
	}

	@Override
	//TODO move to sesam
	protected MutableReferenceType getConverterProviderContextReference() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableExecutableElement converterProviderContextMethod = processingEnv.getElementUtils().getExecutableElement(GET_CONVERTER_PROVIDER_CONTEXT_METHOD);
		converterProviderContextMethod.asType().setReturnType(new ConverterProviderContextType(serviceTypeElement, processingEnv));
	
		ParameterElement[] generatedParameters = new HasConstructorParametersDelegate().getRequiredParameters(processingEnv,
				parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR),
				parametersResolverProvider.getParameterResolver(UsageType.DEFINITION));

		MutableReferenceType[] referenceParams = new MutableReferenceType[generatedParameters.length];
		
		for (int i = 0; i < generatedParameters.length; i++) {
			//TODO handle cast
			MutableReferenceType usage = (MutableReferenceType) generatedParameters[i].getUsage(new ParameterUsageContext() {

				@Override
				public ExecutableElement getMethod() {
					return null;
				}
				
			});
			
			referenceParams[i] = usage;
		}
		
		return typeUtils.getReference(typeUtils.getReferenceToMethod(converterProviderContextMethod, referenceParams), CONVERTER_PROVIDER_CONTEXT_REFERENCE);
	}
}