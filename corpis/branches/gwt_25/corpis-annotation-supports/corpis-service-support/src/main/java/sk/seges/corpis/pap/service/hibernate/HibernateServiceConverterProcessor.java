package sk.seges.corpis.pap.service.hibernate;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.model.printer.converter.HibernateConverterProviderPrinter;
import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderParameterResolver;
import sk.seges.corpis.pap.service.hibernate.printer.HibernateServiceMethodConverterPrinter;
import sk.seges.corpis.pap.service.hibernate.printer.ServiceConverterProviderMethodPrinter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
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

	@Override
	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider(final ServiceTypeElement serviceTypeElement) {
		return new CacheableConverterConstructorParametersResolverProvider() {
			
			@Override
			public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
				switch (usageType) {
					case USAGE_OUTSIDE_CONVERTER_PROVIDER:
						return new HibernateServiceConverterProviderParameterResolver(processingEnv, serviceTypeElement);
					case USAGE_CONSTRUCTOR_CONVERTER_PROVIDER:
						return new HibernateServiceParameterResolver(processingEnv) {
							@Override
							protected boolean isConverterCacheParameterPropagated() {
								return true;
							}
						};
					case USAGE_INSIDE_CONVERTER_PROVIDER:
						return new HibernateServiceParameterResolver(processingEnv) {
							@Override
							protected MutableReferenceType getConverterProviderReference() {
								return processingEnv.getTypeUtils().getReference(null, THIS);
							};
						};
					default:
						return new HibernateServiceParameterResolver(processingEnv);
				}
			}
		};
	}
	
	@Override
	protected ServiceConverterElementPrinter[] getElementPrinters(FormattedPrintWriter pw, final ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(pw),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement), pw),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement), pw),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement), pw),
				new HibernateServiceMethodConverterPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), pw, getConverterProviderPrinter(pw, serviceTypeElement)),
				new ServiceConverterProviderMethodPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), pw, converterProviderPrinter)
		};
	}
	
	@Override
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw, ServiceTypeElement serviceTypeElement) {
		return new HibernateConverterProviderPrinter(pw, processingEnv, getParametersResolverProvider(serviceTypeElement), UsageType.USAGE_OUTSIDE_CONVERTER_PROVIDER);
	}
}