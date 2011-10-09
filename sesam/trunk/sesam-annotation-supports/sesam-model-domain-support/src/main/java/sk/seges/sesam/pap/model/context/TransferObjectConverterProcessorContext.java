package sk.seges.sesam.pap.model.context;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType.RenameActionType;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public class TransferObjectConverterProcessorContext extends TransferObjectProcessorContext {

	public TransferObjectConverterProcessorContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method) {
		super(configurationTypeElement, modifier, method);
	}

	public TransferObjectConverterProcessorContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		super(configurationTypeElement, modifier, method, domainMethod);
	}
	
	@Override
	protected DtoType handleDomainTypeParameter(TransferObjectProcessingEnvironment processingEnv, TransferObjectHelper toHelper, EntityResolver entityResolver, ConfigurationProvider[] configurationProviders) {
		
		TypeParametersSupport typeParametersSupport = new TypeParametersSupport(processingEnv);

		DomainDeclaredType domainTypeElement = configurationTypeElement.getDomain();

		if (domainTypeElement.asType().getKind().equals(TypeKind.DECLARED) && ((DeclaredType)domainTypeElement.asType()).getTypeArguments().size() > 0) {
			
			DeclaredType declaredDomainType = ((DeclaredType)domainTypeElement.asType());
			
			TypeMirror returnType = getDtoMethod().getReturnType();

			switch (returnType.getKind()) {
				case DECLARED:
					DtoType type = processingEnv.getTransferObjectUtils().getDomainType(returnType).getDto();
					
					if (type instanceof MutableDeclaredType) {
		
						for (TypeMirror typeParameter: declaredDomainType.getTypeArguments()) {
							if (typeParameter.getKind().equals(TypeKind.TYPEVAR)) {
								String variable = ((TypeVariable)typeParameter).asElement().getSimpleName().toString();
							
								((MutableDeclaredType)type).renameTypeParameter(RenameActionType.REPLACE, ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + variable, variable, true);
							}
						}
						
						//TODO, check whether type does not contains other type parameters then replaced ones and
						//try to erasure them
						
						return type;
					}
					break;
				case TYPEVAR:
					
					TypeVariable typeVariable = (TypeVariable)returnType;
					
					String variable = typeVariable.asElement().getSimpleName().toString();
					
					if (variable == null || variable.equals("?")) {
						processingEnv.getMessager().printMessage(Kind.WARNING, "Method " + getDtoMethod().getSimpleName().toString() + 
								" returns unsupported type variable " + typeVariable.toString(), configurationTypeElement.asElement());
						return null;
					}
			
					if (typeParametersSupport.hasParameterByName(declaredDomainType, variable)) {
						return processingEnv.getTransferObjectUtils().getDtoType(processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + variable));
					}
					break;
			}
		}

		return super.handleDomainTypeParameter(processingEnv, toHelper, entityResolver, configurationProviders);
	}
}