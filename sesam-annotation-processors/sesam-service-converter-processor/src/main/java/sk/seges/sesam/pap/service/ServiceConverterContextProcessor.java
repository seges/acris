package sk.seges.sesam.pap.service;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.pap.AbstractTransferProcessingProcessor;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.model.annotation.ConverterProviderDefinition;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.configurer.ServiceConverterContextProcessorConfigurer;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceConverterTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ConverterContextElementPrinter;
import sk.seges.sesam.pap.service.printer.converterprovider.ServiceConverterProviderContextPrinter;
import sk.seges.sesam.pap.service.provider.ServiceCollectorConfigurationProvider;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterContextProcessor extends AbstractTransferProcessingProcessor {

    @Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceConverterContextProcessorConfigurer();
	}

    @Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ConverterProviderContextType(context.getMutableType(), processingEnv)
		};
	}

    @Override
    protected void processElement(ProcessorContext context) {

        ConverterProviderContextType contextType = (ConverterProviderContextType)context.getOutputType();

        for (ConverterContextElementPrinter elementPrinter: getElementPrinters(contextType)) {
            elementPrinter.initialize(contextType);

            Set<? extends Element> converterProviders = getClassPathTypes().getElementsAnnotatedWith(ConverterProviderDefinition.class);

            for (Element converterProvider: converterProviders) {
                elementPrinter.print(new ConverterProviderType(converterProvider, processingEnv));
            }

            elementPrinter.finish(contextType);
        }
    }

    protected ConverterContextElementPrinter[] getElementPrinters(MutableDeclaredType contextType) {
		return new ConverterContextElementPrinter[] {
				new ServiceConverterProviderContextPrinter(processingEnv, getParametersResolverProvider(contextType), getClassPathTypes())
		};
	}

    protected ConverterConstructorParametersResolverProvider getParametersResolverProvider(MutableDeclaredType contextType) {
        return new CacheableConverterConstructorParametersResolverProvider() {
            @Override
            public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
                return new DefaultConverterConstructorParametersResolver(processingEnv);
            }
        };
    }

    @Override
    protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
        return new ConfigurationProvider[] {
                new ClasspathConfigurationProvider(getClassPathTypes(), context)
        };
    }
}