package sk.seges.sesam.pap.model;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.printer.ConstructorPrinter;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.equals.ConverterEqualsPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoPrinter;
import sk.seges.sesam.pap.model.provider.TransferObjectConverterProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
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
			public TypeMirror getIterableDtoType(TypeMirror collectionType) {
				return null;
			}
		};
	}
	
	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(element, processingEnv, roundEnv);
		
		ConverterTypeElement converter = configurationTypeElement.getConverter();
		if (!converter.isGenerated()) {
			return supportProcessorChain();
		}

		return super.processElement(element, roundEnv);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(typeElement, processingEnv, roundEnv);
		
		switch (type) {
		case OUTPUT_SUPERCLASS:
			
			ImmutableType domainParameterType = getDomainType(configurationTypeElement);
			ImmutableType dtoParameterType = getDtoType(configurationTypeElement);
			
			return new Type[] {
					TypedClassBuilder.get(BasicCachedConverter.class, dtoParameterType, domainParameterType)
			};
		}
		return super.getOutputDefinition(type, typeElement);
	}
	
	//TODO move to the configuration type element
	protected ImmutableType getDtoType(ConfigurationTypeElement configurationElement) {
		DtoTypeElement dtoType = configurationElement.getDto();
		
		if (dtoType != null) {
			return typeParametersSupport.prefixTypeParameter(dtoType, ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
		}
		return null;
	}

	//TODO move to the configuration type element
	protected ImmutableType getDomainType(ConfigurationTypeElement configurationElement) {
		DomainTypeElement domainType = configurationElement.getDomain();
		
		if (domainType != null) {
			return typeParametersSupport.prefixTypeParameter(domainType, ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
		}
		
		return null;
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected ElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new ElementPrinter[] {
				new ConverterEqualsPrinter(converterProviderPrinter, getEntityResolver(), processingEnv, pw),
				new CopyToDtoPrinter(converterProviderPrinter, getElementTypeConverter(),getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw),
				new CopyFromDtoPrinter(converterProviderPrinter, getEntityResolver(), getParametersResolver(), roundEnv, processingEnv, pw)
		};
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType immutableType) {
		return new NamedType[] { 
				new ConfigurationTypeElement((TypeElement)((DeclaredType)immutableType.asType()).asElement(), processingEnv, roundEnv).getConverter()
		};
	}

	protected TransferObjectProcessorContextProvider getProcessorContextProvider(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectConverterProcessorContextProvider(processingEnv, roundEnv, getEntityResolver());
	}

	protected ParametersResolver getParametersResolver() {
		return new DefaultParametersResolver(processingEnv);
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {

		converterProviderPrinter = new ConverterProviderPrinter(pw, processingEnv, roundEnv, getParametersResolver());

		TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
		
		ParameterElement[] constructorAditionalParameters = getParametersResolver().getConstructorAditionalParameters(new ConfigurationTypeElement(element, processingEnv, roundEnv).getDomain().asType());
		
		ConstructorPrinter constructorPrinter = new ConstructorPrinter(pw, outputName);
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
		
		
		super.processElement(element, outputName, roundEnv, pw);
		
		converterProviderPrinter.printConverterMethods(false);
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