package sk.seges.corpis.pap.service.hibernate;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.model.printer.converter.HibernateConverterProviderPrinter;
import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderParameterResolver;
import sk.seges.corpis.pap.service.hibernate.printer.HibernateServiceMethodConverterPrinter;
import sk.seges.corpis.pap.service.hibernate.printer.ServiceConverterProviderContextMethodPrinter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateServiceParameterResolver;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.ServiceConverterProcessor;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateServiceConverterProcessor extends ServiceConverterProcessor {

	private ConverterConstructorParametersResolverProvider converterConstructorParametersResolverProvider;
	
	@Override
	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider(final ServiceTypeElement serviceTypeElement) {
		
		if (converterConstructorParametersResolverProvider == null) {
			converterConstructorParametersResolverProvider = new CacheableConverterConstructorParametersResolverProvider() {
			
				@Override
				public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
					switch (usageType) {
						case CONVERTER_PROVIDER_OUTSIDE_USAGE:
							return new HibernateServiceConverterProviderParameterResolver(getParametersResolverProvider(serviceTypeElement), 
									processingEnv, serviceTypeElement);
						case CONVERTER_PROVIDER_CONSTRUCTOR_USAGE:
							return new HibernateServiceParameterResolver(processingEnv) {
								@Override
								protected boolean isConverterCacheParameterPropagated() {
									return true;
								}
							};
						case CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR:
							return new HibernateServiceParameterResolver(processingEnv) {
								
								@Override
								protected boolean isConverterCacheParameterPropagated() {
									return true;
								}
								
								@Override
								protected boolean isTransactionPropagationPropagated() {
									return true;
								}

								@Override
								protected boolean isConverterProviderContextParameterPropagated() {
									return false;
								}
							};
						case CONVERTER_PROVIDER_INSIDE_USAGE:
							return new HibernateServiceParameterResolver(processingEnv);
						case CONVERTER_PROVIDER_CONSTRUCTOR: 
							return new HibernateServiceParameterResolver(processingEnv) {

								@Override
								protected boolean isTransactionPropagationPropagated() {
									return true;
								}

								@Override
								protected boolean isConverterProviderContextParameterPropagated() {
									return true;
								}

								@Override
								protected boolean isConverterCacheParameterPropagated() {
									return true;
								}

								@Override
								protected MutableReferenceType getConverterProviderContextReference() {
									return processingEnv.getTypeUtils().getReference(null, THIS);
								}
							};
						default:
							return new HibernateServiceParameterResolver(processingEnv);
					}
				}
			};
		}
		
		return converterConstructorParametersResolverProvider;
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		converterConstructorParametersResolverProvider = null;
		super.processElement(context);
	}
	
	@Override
	protected ServiceConverterElementPrinter[] getElementPrinters(final ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new HibernateServiceMethodConverterPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), getConverterProviderPrinter(serviceTypeElement)),
				new ServiceConverterProviderContextMethodPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), converterProviderPrinter, getClassPathTypes())
		};
	}
	
	@Override
	protected ConverterProviderPrinter getConverterProviderPrinter(ServiceTypeElement serviceTypeElement) {
		return new HibernateConverterProviderPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), UsageType.CONVERTER_PROVIDER_OUTSIDE_USAGE);
	}
}