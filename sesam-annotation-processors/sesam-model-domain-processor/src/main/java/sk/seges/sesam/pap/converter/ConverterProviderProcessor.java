package sk.seges.sesam.pap.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.AbstractTransferProcessingProcessor;
import sk.seges.sesam.pap.converter.configurer.ConverterProviderProcessorConfigurer;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.converter.model.HasConstructorParameters;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.ConverterProviderPrinterDelegate;
import sk.seges.sesam.pap.converter.printer.converterprovider.DomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.DtoMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.annotation.ConverterProviderDefinition;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConverterProviderProcessor extends AbstractTransferProcessingProcessor {

	protected ConverterProviderPrinter converterProviderPrinter;
	protected ConfigurationProvider[] lookupConfigurationProviders = null;

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

	@Override
	protected void printAnnotations(ProcessorContext context) {
		super.printAnnotations(context);
		context.getPrintWriter().println("@", ConverterProviderDefinition.class);
	}

	protected final MutableReferenceType getThisReference() {
		return processingEnv.getTypeUtils().getReference(null, ConverterConstructorParametersResolver.THIS);
	}
	
	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider() {
		return new CacheableConverterConstructorParametersResolverProvider() {

			@Override
			public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
				switch (usageType) {
					case CONVERTER_PROVIDER_INSIDE_USAGE:
						return new DefaultConverterConstructorParametersResolver(processingEnv) {
	
							@Override
							protected boolean isConverterCacheParameterPropagated() {
								return false;
							}
						};

					case CONVERTER_PROVIDER_OUTSIDE_USAGE:
						return new DefaultConverterConstructorParametersResolver(processingEnv);
					default:
						return new DefaultConverterConstructorParametersResolver(processingEnv);
				}
			}
		};
		
	}
	
	protected ConverterProviderPrinter getConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv) {
		return new ConverterProviderPrinter(processingEnv, getParametersResolverProvider(), UsageType.CONVERTER_PROVIDER_INSIDE_USAGE) {
			@Override
			protected List<ConverterConstructorParameter> getConverterProviderMethodAdditionalParameters(ConverterTypeElement converterTypeElement, ConverterTargetType converterTargetType) {
				return new ArrayList<ConverterConstructorParameter>();
			}
		};
	}

	protected ConverterProviderPrinter ensureConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv) {
		if (converterProviderPrinter == null) {
			converterProviderPrinter = getConverterProviderPrinter(processingEnv);
		}
		
		return converterProviderPrinter;
	}

	protected ConverterProviderElementPrinter[] getNestedPrinters(FormattedPrintWriter pw) {
		return new ConverterProviderElementPrinter[] {
			new DomainMethodConverterProviderPrinter(getParametersResolverProvider(), processingEnv, pw, ensureConverterProviderPrinter(processingEnv)),
			new DtoMethodConverterProviderPrinter(getParametersResolverProvider(), processingEnv, pw, ensureConverterProviderPrinter(processingEnv))
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		ConverterProviderPrinterDelegate converterProviderPrinterDelegate = new ConverterProviderPrinterDelegate(getParametersResolverProvider());
		converterProviderPrinterDelegate.initialize(environmentContext.getProcessingEnv(), (HasConstructorParameters) context.getOutputType(), UsageType.DEFINITION);

		for (ConverterProviderElementPrinter nestedElementPrinter: getNestedPrinters(context.getPrintWriter())) {

			nestedElementPrinter.initialize();

			List<String> processedConfigurations = new ArrayList<String>();
			for (ConfigurationProvider configurationProvider: getLookupConfigurationProviders(context.getMutableType(), getEnvironmentContext(context.getMutableType()))) {
				List<ConfigurationTypeElement> availableConfigurations = configurationProvider.getAvailableConfigurations();
				
				for (ConfigurationTypeElement availableConfiguration: availableConfigurations) {
					if (!processedConfigurations.contains(availableConfiguration.getCanonicalName())) {
						
						List<ConfigurationTypeElement> configurationsForDomain = configurationProvider.getConfigurationsForDomain(availableConfiguration.getInstantiableDomain());

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
		
		ConverterProviderPrinter converterProviderPrinter = ensureConverterProviderPrinter(processingEnv);
		UsageType previousUsage = converterProviderPrinter.changeUsage(UsageType.CONVERTER_PROVIDER_OUTSIDE_USAGE);
//		converterProviderPrinter.printConverterMethods(false, ConverterProviderMethodType .GET, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
		converterProviderPrinter.printConverterMethods(context.getOutputType().getPrintWriter(), false, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
		converterProviderPrinter.changeUsage(previousUsage);
		converterProviderPrinterDelegate.finalize();
	}

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
			new ClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext(mutableType))
		};
	}

	protected ConfigurationProvider[] getLookupConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
			new RoundEnvConfigurationProvider(getEnvironmentContext(mutableType))
		};
	}
}