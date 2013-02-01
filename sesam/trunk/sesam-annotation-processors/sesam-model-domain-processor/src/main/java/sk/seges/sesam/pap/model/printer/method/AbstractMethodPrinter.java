package sk.seges.sesam.pap.model.printer.method;

import java.util.Iterator;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.AbstractDtoPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.utils.CastUtils;

public abstract class AbstractMethodPrinter extends AbstractDtoPrinter {

	protected final RoundEnvironment roundEnv;
	protected final TypeParametersSupport typeParametersSupport;
	protected final EntityResolver entityResolver;
	
	protected ConverterProviderPrinter converterProviderPrinter;
	
	protected AbstractMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ConverterConstructorParametersResolverProvider parametersResolverProvider, EntityResolver entityResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(parametersResolverProvider, processingEnv);
		this.roundEnv = roundEnv;
		this.converterProviderPrinter = converterProviderPrinter;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
		this.entityResolver = entityResolver;
	}

	protected boolean isIdField(MutableDeclaredType typeElement, String fieldName) {
		if (typeElement.asElement() != null) {
			ExecutableElement method = ProcessorUtils.getMethod(fieldName, typeElement.asElement());
			if (method != null) {
				return  entityResolver.isIdMethod(method);
			}
		}
		return false;
	}
	
	protected MutableTypeMirror getWildcardDelegate(MutableTypeMirror domainDelegate) {
		if (domainDelegate.getKind().isDeclared() && ((MutableDeclaredType)domainDelegate).getTypeVariables().size() == 1) {
			return ((MutableDeclaredType)domainDelegate).clone().setTypeVariables(processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, (MutableTypeMirror)null));
		}
		
		return domainDelegate;
	}
	
	protected MutableTypeMirror getTypeVariableDelegate(MutableTypeMirror domainDelegate) {
		if (domainDelegate.getKind().isDeclared() && ((MutableDeclaredType)domainDelegate).getTypeVariables().size() == 1) {
			MutableTypeVariable mutableTypeVariable = ((MutableDeclaredType)domainDelegate).getTypeVariables().get(0);
			if (mutableTypeVariable.getUpperBounds().size() == 1) {
				return mutableTypeVariable.getUpperBounds().iterator().next();
			}
			if (mutableTypeVariable.getLowerBounds().size() == 1) {
				return mutableTypeVariable.getLowerBounds().iterator().next();
			}
			return mutableTypeVariable;
		}
		
		if (domainDelegate.getKind().isDeclared()) {
			return ((MutableDeclaredType)domainDelegate).clone().setTypeVariables();
		}
		
		return domainDelegate;
	}
		
	protected MutableTypeMirror getDelegateCast(MutableTypeMirror domainNamedType, boolean stripWildcard) {
		TypeMirror domainType = processingEnv.getTypeUtils().fromMutableType(domainNamedType);
		
		if (domainType == null) {
			return domainNamedType;
		}
		
		DomainType domainTypeElement = processingEnv.getTransferObjectUtils().getDomainType(domainType);

		return castToDelegate(domainNamedType, domainTypeElement.getDomainDefinitionConfiguration() == null ? null : 
			domainTypeElement.getDomainDefinitionConfiguration().getDelegateConfigurationTypeElement(), stripWildcard);

	}
	
	public MutableTypeMirror getDelegateCast(TypeMirror domainType) {
		DomainType domainTypeElement = processingEnv.getTransferObjectUtils().getDomainType(domainType);

		MutableTypeMirror domainNamedType = processingEnv.getTypeUtils().toMutableType(domainType);

		return castToDelegate(domainNamedType, domainTypeElement.getDomainDefinitionConfiguration() == null ? null : 
			domainTypeElement.getDomainDefinitionConfiguration().getDelegateConfigurationTypeElement(), true);
	}
	
	/**
	 * Method is used to handle domain types with type variables (generics). If there is need to cast from
	 * List<A> to the List<B> we should call {@link CastUtils}.cast(list, B.class) and this method will resolve
	 * type variable from target domain type.
	 */
	private MutableTypeMirror castToDelegate(MutableTypeMirror domainNamedType, ConfigurationTypeElement delegateConfigurationTypeElement, boolean stripWildcard) {

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
						convertedBounds[i++] = getDelegateCast(iterator.next(), stripWildcard);
					}

					if (typeParameter.getVariable() != null && typeParameter.getVariable().equals(MutableWildcardType.WILDCARD_NAME) && stripWildcard) {
						convertedParameters[j++] = processingEnv.getTypeUtils().getTypeVariable(null, convertedBounds);
					} else {
						convertedParameters[j++] = processingEnv.getTypeUtils().getTypeVariable(typeParameter.getVariable(), convertedBounds);
					}
				} else {
					convertedParameters[j++] = processingEnv.getTypeUtils().getTypeVariable(typeParameter.getVariable());
				}
			}
			
			domainNamedType = processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)domainNamedType, convertedParameters);
		}
		
		return domainNamedType;
	}

	private static final String RESULT = "_result";
	
	protected void printDtoInstancer(FormattedPrintWriter pw, EntityResolver entityResolver, DtoType type) {
		pw.println(type," " + RESULT + " = new ", type, "();");
		if ((type instanceof DtoDeclaredType) && entityResolver.shouldHaveIdMethod((DomainDeclaredType) type.getDomainDefinitionConfiguration().getInstantiableDomain())) {
			MutableExecutableElement idMethod = ((DtoDeclaredType)type).getIdMethod(entityResolver);			
			pw.println(RESULT + "." + MethodHelper.toSetter(idMethod) + "((", idMethod.asType().getReturnType(), ")id);");
		}
		pw.println("return " + RESULT + ";");
	}

	protected boolean copy(TransferObjectContext context, FormattedPrintWriter pw, CopyMethodPrinter printer) {
		
		DomainType returnType = context.getDomainMethodReturnType();
		
		switch (returnType.getKind()) {
		case VOID:
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process " + context.getDtoFieldName() + ". Unsupported result type: " + 
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