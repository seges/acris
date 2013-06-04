package sk.seges.sesam.pap.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.AbstractTransferProcessingProcessor;
import sk.seges.sesam.pap.converter.configurer.ConverterProviderProcessorConfigurer;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.ConverterProviderPrinterDelegate;
import sk.seges.sesam.pap.converter.printer.converterprovider.DomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterFilterType;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.ConverterProviderType;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConverterProviderProcessor extends AbstractTransferProcessingProcessor {

	private ConverterProviderPrinter converterProviderPrinter;
	
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

	protected ConverterConstructorParametersResolver getParametersResolver() {
		return new ServiceConverterConstructorParametersResolver(processingEnv);
	}	

	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv) {
		if (converterProviderPrinter == null) {
			converterProviderPrinter = new ConverterProviderPrinter(pw, processingEnv, getParametersResolver());
		}
		
		return converterProviderPrinter;
	}

	protected ConverterProviderElementPrinter[] getNestedPrinters(FormattedPrintWriter pw) {
		return new ConverterProviderElementPrinter[] {
			new DomainMethodConverterProviderPrinter(getParametersResolver(), processingEnv, pw, getConverterProviderPrinter(pw, processingEnv))/*,
			new ServiceMethodDtoConverterProviderPrinter(getParametersResolver(), processingEnv, pw, converterProviderPrinter)	*/
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
		
		getConverterProviderPrinter(context.getPrintWriter(), processingEnv).printConverterMethods(false, ConverterFilterType.ENSURED, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
		converterProviderPrinterDelegate.finalize();
	}

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new ClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext(mutableType))
		};
	}
}
