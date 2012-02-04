package sk.seges.sesam.pap.model;

import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

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
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.equals.ConverterEqualsPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoPrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectConverterProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.BasicConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectConverterProcessor extends AbstractTransferProcessor {

	protected ConverterProviderPrinter converterProviderPrinter;

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
				new ConverterEqualsPrinter(converterProviderPrinter, getEntityResolver(), processingEnv, pw),
				new CopyToDtoPrinter(converterProviderPrinter, getElementTypeConverter(),getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw),
				new CopyFromDtoPrinter(converterProviderPrinter, getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw)
		};
	}
	
	protected TransferObjectProcessorContextProvider getProcessorContextProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectConverterProcessorContextProvider(getEnvironmentContext(), getEntityResolver());
	}

	protected ParametersResolver getParametersResolver() {
		return new DefaultParametersResolver(processingEnv);
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
		
		ConstructorPrinter constructorPrinter = new ConstructorPrinter(pw, context.getOutputType());
		constructorPrinter.printConstructors(cachedConverterType, constructorAditionalParameters);

		List<ExecutableElement> constructors = ElementFilter.constructorsIn(cachedConverterType.getEnclosedElements());

		if (constructors.size() > 0) {
			ExecutableElement constructor = constructors.iterator().next();
			
			TypeElement basicConverterType = processingEnv.getElementUtils().getTypeElement(BasicConverter.class.getCanonicalName());
			constructorPrinter.printConstructors(basicConverterType, constructor, false, constructorAditionalParameters);
			
			for (VariableElement parameter: constructor.getParameters()) {
				String name = parameter.getSimpleName().toString();
				if (!existsField(name, constructor)) {
					pw.println("private ", parameter.asType(), " " + name + ";");
					pw.println();
				}
			}
		}
		
		for (ParameterElement parameter: constructorAditionalParameters) {
			pw.println("private ", parameter.getType(), " " + parameter.getName().toString() + ";");
			pw.println();
		}
		
		
		super.processElement(context);
		
		converterProviderPrinter.printConverterMethods(false, 0);
	}
	
	//TODO same method as in method helper
	private boolean existsField(String field, ExecutableElement constructor) {
		List<VariableElement> fields = ElementFilter.fieldsIn(constructor.getEnclosingElement().getEnclosedElements());
		
		for (VariableElement classField: fields) {
			if (classField.getSimpleName().equals(field)) {
				return true;
			}
		}
		
		return false;
	}
}