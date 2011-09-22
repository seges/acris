package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public abstract class AbstractMethodPrinter {

	protected final ProcessingEnvironment processingEnv;
	protected final NameTypeUtils nameTypesUtils;
	protected final RoundEnvironment roundEnv;
	protected final ParametersResolver parametersResolver;
	protected final TypeParametersSupport typeParametersSupport;
	protected final TransferObjectHelper toHelper;
	
	protected ConverterProviderPrinter converterProviderPrinter;
	
	protected AbstractMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ParametersResolver parametersResolver, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.converterProviderPrinter = converterProviderPrinter;
		this.parametersResolver = parametersResolver;
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		this.toHelper = new TransferObjectHelper(nameTypesUtils, processingEnv, roundEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, nameTypesUtils);
	}

	protected NamedType castToDelegate(NamedType domainNamedType) {
		TypeElement domainType = processingEnv.getElementUtils().getTypeElement(domainNamedType.getCanonicalName());
		
		if (domainType == null) {
			return domainNamedType;
		}
		
		DomainTypeElement domainTypeElement = new DomainTypeElement(domainType.asType(), processingEnv, roundEnv);

		return castToDelegate(domainNamedType, domainTypeElement.getConfiguration() == null ? null : 
			domainTypeElement.getConfiguration().getDelegateConfigurationTypeElement());
		
	}
	
	public NamedType castToDelegate(TypeMirror domainType) {
		DomainTypeElement domainTypeElement = new DomainTypeElement(domainType, processingEnv, roundEnv);

		NamedType domainNamedType = nameTypesUtils.toType(domainType);

		return castToDelegate(domainNamedType, domainTypeElement.getConfiguration() == null ? null : 
			domainTypeElement.getConfiguration().getDelegateConfigurationTypeElement());
	}
	
	protected NamedType castToDelegate(NamedType domainNamedType, ConfigurationTypeElement delegateConfigurationTypeElement) {

		if (delegateConfigurationTypeElement != null) {
			DomainTypeElement replacementType = delegateConfigurationTypeElement.getDomain();
			
			if (typeParametersSupport.hasTypeParameters(domainNamedType) && typeParametersSupport.hasTypeParameters(replacementType)) {
				domainNamedType = TypedClassBuilder.get(replacementType, ((HasTypeParameters)domainNamedType).getTypeParameters());
			} else {
				domainNamedType = replacementType;
			}
		}
		
		if (typeParametersSupport.hasTypeParameters(domainNamedType)) {

			TypeParameter[] convertedParameters = new TypeParameter[((HasTypeParameters) domainNamedType).getTypeParameters().length];
			
			int j = 0;
			for (TypeParameter typeParameter: ((HasTypeParameters) domainNamedType).getTypeParameters()) {
				
				if (typeParameter.getBounds() != null) {
					NamedType[] convertedBounds = new NamedType[typeParameter.getBounds().length];
					
					int i = 0;
					for (TypeVariable typeVariable: typeParameter.getBounds()) {
						convertedBounds[i++] = castToDelegate(nameTypesUtils.toType(typeVariable.getUpperBound()));
					}
					
					convertedParameters[j++] = TypeParameterBuilder.get(typeParameter.getVariable(), convertedBounds);
				} else {
					convertedParameters[j++] = TypeParameterBuilder.get(typeParameter.getVariable());
				}
			}
			
			domainNamedType = TypedClassBuilder.get(domainNamedType, convertedParameters);
		}
		
		return domainNamedType;
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

	protected void printDtoInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.toString(ClassSerializer.SIMPLE, true) + "();");
	}

	protected boolean copy(ProcessorContext context, FormattedPrintWriter pw, CopyMethodPrinter printer) {
		
		TypeMirror returnType = context.getDomainMethodReturnType();
		
		switch (returnType.getKind()) {
		case ERROR:
		case NONE:
		case NULL:
		case OTHER:
		case VOID:
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process " + context.getFieldName() + ". Unsupported result type: " + 
					returnType.getKind());
			return false;
		case ARRAY:
			//TODO
			return false;
		}
		
		printer.printCopyMethod(context, pw);
		
		return true;
	}
}