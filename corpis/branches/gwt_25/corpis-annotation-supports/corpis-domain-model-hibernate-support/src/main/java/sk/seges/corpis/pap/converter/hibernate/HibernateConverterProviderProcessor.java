package sk.seges.corpis.pap.converter.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.converter.hibernate.resolver.HibernateConverterParameterResolver;
import sk.seges.corpis.pap.model.printer.converter.HibernateConverterProviderPrinter;
import sk.seges.sesam.core.pap.model.ConverterParameter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.ConverterProviderProcessor;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DtoMethodConverterProviderPrinter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateConverterProviderProcessor extends ConverterProviderProcessor {

	@Override
	protected ConverterConstructorParametersResolver getParametersResolver() {
		return new HibernateConverterParameterResolver(processingEnv);
	}
	
	@Override
	protected ConverterConstructorParametersResolver getConverterMethodParametersResolver() {
		return new HibernateConverterParameterResolver(processingEnv) {

			@Override
			protected boolean isConverterCacheParameterPropagated() {
				return false;
			}
		};
	}	

	@Override
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv) {
		return new HibernateConverterProviderPrinter(pw, processingEnv, getConverterMethodParametersResolver()) {

			@Override
			protected List<ConverterParameter> getConverterProviderMethodAdditionalParameters(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
				return new ArrayList<ConverterParameter>();
			}
		};
	}

	protected ConverterProviderElementPrinter[] getNestedPrinters(FormattedPrintWriter pw) {
		return new ConverterProviderElementPrinter[] {
			new DomainMethodConverterProviderPrinter(getConverterMethodParametersResolver(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv)),
			new DtoMethodConverterProviderPrinter(getConverterMethodParametersResolver(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv))
		};
	}
}