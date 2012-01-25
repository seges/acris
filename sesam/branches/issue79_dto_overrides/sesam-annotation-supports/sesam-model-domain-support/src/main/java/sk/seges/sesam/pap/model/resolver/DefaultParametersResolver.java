package sk.seges.sesam.pap.model.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class DefaultParametersResolver implements ParametersResolver {

	protected MutableProcessingEnvironment processingEnv;
	public static final String CONVERTER_PROVIDER_NAME = "converterProvider";

	public DefaultParametersResolver(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
			return new ParameterElement[0];
		}
		List<ParameterElement> variableElements = new ArrayList<ParameterElement>();

//		DeclaredType declaredDomainType = (DeclaredType)domainType;
		
//		if (declaredDomainType.getTypeArguments().size() > 0) {

			MutableTypes typeUtils = processingEnv.getTypeUtils();
			
			variableElements.add(new ParameterElement(typeUtils.toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, true));

//			TypeElement dtoConverterType = processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName());
//			
//			int index = 0;
//			for (TypeMirror typeParameterElement: declaredDomainType.getTypeArguments()) {
//
//				if (typeParameterElement.getKind().equals(TypeKind.TYPEVAR)) {
//					String typeParameterName = ((TypeVariable)typeParameterElement).asElement().getSimpleName().toString();
//					
//					if (typeParameterName != null && !typeParameterName.equals("?") && typeParameterName.length() > 0) {
//						MutableTypeVariable dtoTypeVariable = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + typeParameterName);
//						MutableTypeVariable domainTypeVariable = processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + typeParameterName);
//						
//						MutableDeclaredType dtoConverterNamedType = processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType((DeclaredType) dtoConverterType.asType()), dtoTypeVariable, domainTypeVariable);
//						variableElements.add(new ConverterParameterElement(dtoConverterNamedType, TransferObjectContext.LOCAL_CONVERTER_NAME + index++, true));
//					} else {
//						//TODO handle type parameter with no variables
//					}
//				} else {
//					//TODO handle type parameter with no variables
//				}
//			}
//		}
		
		return variableElements.toArray(new ParameterElement[] {});
	}
}