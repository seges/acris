package sk.seges.sesam.pap.model;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.printer.ConstructorPrinter;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.equals.ConverterEqualsPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoPrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectConverterProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectConverterProcessor extends AbstractTransferProcessor {

	protected ConverterProviderPrinter converterProviderPrinter;
	protected Set<String> nestedInstances = new HashSet<String>();

	protected ElementHolderTypeConverter getElementTypeConverter() {
		return new ElementHolderTypeConverter() {

			@Override
			public TypeMirror getIterableDtoType(MutableTypeMirror collectionType) {
				return null;
			}
		};
	}
	
	protected ConfigurationProvider[] getConfigurationProviders() {
		return new ConfigurationProvider[] {
				new ClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext())
		};
	}

	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		
		ConverterTypeElement converter = getConfigurationElement(context).getConverter();
		if (converter == null || !converter.isGenerated()) {
			return false;
		}

		return super.checkPreconditions(context, alreadyExists);
	}
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		
		ConverterTypeElement converter = getConfigurationElement(context).getConverter();
		if (converter == null || !converter.isGenerated()) {
			return new MutableDeclaredType[] {};
		}

		return new MutableDeclaredType[] { converter };
	}
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected TransferObjectElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new TransferObjectElementPrinter[] {
				new ConverterEqualsPrinter(converterProviderPrinter, getEntityResolver(), getParametersResolverProvider(), processingEnv, pw),
				new CopyToDtoPrinter(converterProviderPrinter, getElementTypeConverter(),getEntityResolver(), getParametersResolverProvider(), roundEnv, processingEnv, pw),
				new CopyFromDtoPrinter(nestedInstances, converterProviderPrinter, getEntityResolver(), getParametersResolverProvider(), roundEnv, processingEnv, pw)
		};
	}
	
	protected TransferObjectProcessorContextProvider getProcessorContextProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectConverterProcessorContextProvider(getEnvironmentContext(), getEntityResolver());
	}

	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider() {
		return new CacheableConverterConstructorParametersResolverProvider() {
			
			@Override
			public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
				return new DefaultConverterConstructorParametersResolver(processingEnv);
			}
		};
	}
	
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw) {
		return new ConverterProviderPrinter(processingEnv, getParametersResolverProvider(), UsageType.CONVERTER_PROVIDER_OUTSIDE_USAGE);
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();
		
		converterProviderPrinter = getConverterProviderPrinter(pw);

		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
		
		ParameterElement[] constructorAditionalParameters = getParametersResolverProvider().getParameterResolver(UsageType.DEFINITION).getConstructorAditionalParameters();
		
		for (ParameterElement parameter: constructorAditionalParameters) {
			pw.println("private ", parameter.getType(), " " + parameter.getName().toString() + ";");
			pw.println();
		}

		ConstructorPrinter constructorPrinter = new ConstructorPrinter(pw, context.getOutputType(),processingEnv);
		constructorPrinter.printConstructors(cachedConverterType, constructorAditionalParameters);

		super.processElement(context);
		
//		converterProviderPrinter.printConverterMethods(false, ConverterProviderMethodType.ALL, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
		converterProviderPrinter.printConverterMethods(pw, false, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
	}	
}