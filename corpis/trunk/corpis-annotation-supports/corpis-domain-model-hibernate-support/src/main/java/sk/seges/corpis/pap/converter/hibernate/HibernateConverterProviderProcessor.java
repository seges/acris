package sk.seges.corpis.pap.converter.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.converter.hibernate.resolver.HibernateConverterParameterResolver;
import sk.seges.corpis.pap.model.printer.converter.HibernateConverterProviderPrinter;
import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.ConverterProviderProcessor;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DtoMethodConverterProviderPrinter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateConverterProviderProcessor extends ConverterProviderProcessor {

	@Override
	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider() {
		return new CacheableConverterConstructorParametersResolverProvider() {

			@Override
			public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
				return new HibernateConverterParameterResolver(processingEnv);
			}
		};
		
	}

	@Override
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv) {
		return new HibernateConverterProviderPrinter(pw, processingEnv, getParametersResolverProvider(), UsageType.CONVERTER_PROVIDER_INSIDE_USAGE) {

			@Override
			protected List<ConverterConstructorParameter> getConverterProviderMethodAdditionalParameters(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
				return new ArrayList<ConverterConstructorParameter>();
			}
		};
	}

	protected ConverterProviderElementPrinter[] getNestedPrinters(FormattedPrintWriter pw) {
		return new ConverterProviderElementPrinter[] {
			new DomainMethodConverterProviderPrinter(getParametersResolverProvider(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv)),
			new DtoMethodConverterProviderPrinter(getParametersResolverProvider(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv))
		};
	}
}