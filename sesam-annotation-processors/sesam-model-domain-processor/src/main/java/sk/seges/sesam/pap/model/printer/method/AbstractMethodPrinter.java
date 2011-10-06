package sk.seges.sesam.pap.model.printer.method;

import java.util.Iterator;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public abstract class AbstractMethodPrinter {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;
	protected final ParametersResolver parametersResolver;
	protected final TypeParametersSupport typeParametersSupport;
	protected final TransferObjectHelper toHelper;
	
	protected ConverterProviderPrinter converterProviderPrinter;
	
	protected AbstractMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ParametersResolver parametersResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.converterProviderPrinter = converterProviderPrinter;
		this.parametersResolver = parametersResolver;
		this.toHelper = new TransferObjectHelper(processingEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
	}

	protected MutableTypeMirror castToDelegate(MutableTypeMirror domainNamedType) {
		TypeMirror domainType = processingEnv.getTypeUtils().fromMutableType(domainNamedType);
		
		if (domainType == null) {
			return domainNamedType;
		}
		
		DomainType domainTypeElement = processingEnv.getTransferObjectUtils().getDomainType(domainType);

		return castToDelegate(domainNamedType, domainTypeElement.getConfiguration() == null ? null : 
			domainTypeElement.getConfiguration().getDelegateConfigurationTypeElement());
		
	}
	
	public MutableTypeMirror castToDelegate(TypeMirror domainType) {
		DomainType domainTypeElement = processingEnv.getTransferObjectUtils().getDomainType(domainType);

		MutableTypeMirror domainNamedType = processingEnv.getTypeUtils().toMutableType(domainType);

		return castToDelegate(domainNamedType, domainTypeElement.getConfiguration() == null ? null : 
			domainTypeElement.getConfiguration().getDelegateConfigurationTypeElement());
	}
	
	protected MutableTypeMirror castToDelegate(MutableTypeMirror domainNamedType, ConfigurationTypeElement delegateConfigurationTypeElement) {

		if (delegateConfigurationTypeElement != null) {
			DomainDeclaredType replacementType = delegateConfigurationTypeElement.getDomain();
			
			if ((domainNamedType instanceof MutableDeclaredType) && ((MutableDeclaredType)domainNamedType).hasTypeParameters() && replacementType.hasTypeParameters()) {
				domainNamedType = replacementType.clone().setTypeVariables(((MutableDeclaredType)domainNamedType).getTypeVariables().toArray(new MutableTypeVariable[] {}));
			} else {
				domainNamedType = replacementType;
			}
		}
		
		if ((domainNamedType instanceof MutableDeclaredType) && ((MutableDeclaredType)domainNamedType).hasTypeParameters()) {

			MutableTypeVariable[] convertedParameters = new MutableTypeVariable[((MutableDeclaredType)domainNamedType).getTypeVariables().size()];
			
			int j = 0;
			for (MutableTypeVariable typeParameter: ((MutableDeclaredType)domainNamedType).getTypeVariables()) {
				
				if (typeParameter.getUpperBounds().size() > 0) {
					MutableTypeMirror[] convertedBounds = new MutableTypeMirror[typeParameter.getUpperBounds().size()];
					
					int i = 0;
					
					Iterator<? extends MutableTypeMirror> iterator = typeParameter.getUpperBounds().iterator();
					
					while (iterator.hasNext()) {
						convertedBounds[i++] = castToDelegate(iterator.next());
					}
					
					convertedParameters[j++] = processingEnv.getTypeUtils().getTypeVariable(typeParameter.getVariable(), convertedBounds);
				} else {
					convertedParameters[j++] = processingEnv.getTypeUtils().getTypeVariable(typeParameter.getVariable());
				}
			}
			
			domainNamedType = processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)domainNamedType, convertedParameters);
		}
		
		return domainNamedType;
	}

	protected void printDtoInstancer(FormattedPrintWriter pw, MutableTypeMirror type) {
		pw.println("return new ", type, "();");
	}

	protected boolean copy(TransferObjectContext context, FormattedPrintWriter pw, CopyMethodPrinter printer) {
		
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