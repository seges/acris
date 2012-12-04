package sk.seges.sesam.pap.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.ConverterParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.AbstractTransferProcessingProcessor;
import sk.seges.sesam.pap.converter.configurer.ConverterProviderProcessorConfigurer;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.ConverterProviderPrinterDelegate;
import sk.seges.sesam.pap.converter.printer.converterprovider.DomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DtoMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderMethodType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConverterProviderProcessor extends AbstractTransferProcessingProcessor {

	protected ConverterProviderPrinter converterProviderPrinter;

	@Override
	public ExecutionType getExecutionType() {
		return ExecutionType.ONCE;
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConverterProviderProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new ConverterProviderType(context.getMutableType(), processingEnv)
		};
	}

	protected final MutableReferenceType getThisReference() {
		return processingEnv.getTypeUtils().getReference(null, ConverterConstructorParametersResolver.THIS);
	}
	
	protected ConverterConstructorParametersResolver getParametersResolver() {
		
		return new DefaultConverterConstructorParametersResolver(processingEnv) {
			@Override
			protected MutableReferenceType getConverterProviderReference() {
				return getThisReference();
			}
		};
	}	

	protected ConverterConstructorParametersResolver getConverterMethodParametersResolver() {
		return new DefaultConverterConstructorParametersResolver(processingEnv) {

			@Override
			protected boolean isConverterCacheParameterPropagated() {
				return false;
			}
			
			@Override
			protected MutableReferenceType getConverterProviderReference() {
				return getThisReference();
			}
		};
	}	

	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv) {
		return new ConverterProviderPrinter(pw, processingEnv, getConverterMethodParametersResolver()) {
			@Override
			protected List<ConverterParameter> getConverterProviderMethodAdditionalParameters(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
				return new ArrayList<ConverterParameter>();
			}
		};
	}

	protected ConverterProviderPrinter ensureConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv) {
		if (converterProviderPrinter == null) {
			converterProviderPrinter = getConverterProviderPrinter(pw, processingEnv);
		}
		
		return converterProviderPrinter;
	}

	protected ConverterProviderElementPrinter[] getNestedPrinters(FormattedPrintWriter pw) {
		return new ConverterProviderElementPrinter[] {
			new DomainMethodConverterProviderPrinter(getConverterMethodParametersResolver(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv)),
			new DtoMethodConverterProviderPrinter(getConverterMethodParametersResolver(), processingEnv, pw, ensureConverterProviderPrinter(pw, processingEnv))
		};
	}

	@Override
	protected void processElement(ProcessorContext context) {

		ConverterProviderPrinterDelegate converterProviderPrinterDelegate = new ConverterProviderPrinterDelegate(getParametersResolver(), context.getPrintWriter());
		ConverterProviderType converterProviderType = new ConverterProviderType(context.getMutableType(), processingEnv);
		converterProviderPrinterDelegate.initialize(converterProviderType);

		for (ConverterProviderElementPrinter nestedElementPrinter: getNestedPrinters(context.getPrintWriter())) {

			nestedElementPrinter.initialize();

			List<String> processedConfigurations = new ArrayList<String>();
			for (ConfigurationProvider configurationProvider: configurationProviders) {
				List<ConfigurationTypeElement> availableConfigurations = configurationProvider.getAvailableConfigurations();
				
				for (ConfigurationTypeElement availableConfiguration: availableConfigurations) {
					if (!processedConfigurations.contains(availableConfiguration.getCanonicalName())) {
						
						List<ConfigurationTypeElement> configurationsForDomain = configurationProvider.getConfigurationsForDomain(availableConfiguration.getDomain());

						for (ConfigurationTypeElement configurationForDomain: configurationsForDomain) {
							if (!processedConfigurations.contains(availableConfiguration.getCanonicalName())) {
								processedConfigurations.add(availableConfiguration.getCanonicalName());
								ConverterProviderPrinterContext printerContext = new ConverterProviderPrinterContext(configurationForDomain.getDto());
								nestedElementPrinter.print(printerContext);
							}
						}
					}
				}
			}

			nestedElementPrinter.finish();

		}
		
		ensureConverterProviderPrinter(context.getPrintWriter(), processingEnv).printConverterMethods(false, ConverterProviderMethodType.GET, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
		converterProviderPrinterDelegate.finalize();
	}

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new RoundEnvConfigurationProvider(getEnvironmentContext(mutableType))
		};
	}
}