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
import sk.seges.sesam.pap.model.printer.converter.ConverterFilterType;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.equals.ConverterEqualsPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoPrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectConverterProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
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
				new ConverterEqualsPrinter(converterProviderPrinter, getEntityResolver(), getParametersResolver(), processingEnv, pw),
				new CopyToDtoPrinter(converterProviderPrinter, getElementTypeConverter(),getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw),
				new CopyFromDtoPrinter(nestedInstances, converterProviderPrinter, getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw)
		};
	}
	
	protected TransferObjectProcessorContextProvider getProcessorContextProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectConverterProcessorContextProvider(getEnvironmentContext(), getEntityResolver());
	}

	protected ConverterConstructorParametersResolver getParametersResolver() {
		return new DefaultConverterConstructorParametersResolver(processingEnv);
	}
	
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw) {
		return new ConverterProviderPrinter(pw, processingEnv, getParametersResolver());
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();
		
		converterProviderPrinter = getConverterProviderPrinter(pw);

		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
		
		ParameterElement[] constructorAditionalParameters = getParametersResolver().getConstructorAditionalParameters();
		
		for (ParameterElement parameter: constructorAditionalParameters) {
			pw.println("private ", parameter.getType(), " " + parameter.getName().toString() + ";");
			pw.println();
		}

		ConstructorPrinter constructorPrinter = new ConstructorPrinter(pw, context.getOutputType(),processingEnv);
		constructorPrinter.printConstructors(cachedConverterType, constructorAditionalParameters);

		super.processElement(context);
		
		converterProviderPrinter.printConverterMethods(false, ConverterFilterType.NONE, ConverterInstancerType.REFERENCED_CONVERTER_INSTANCER);
	}	
}