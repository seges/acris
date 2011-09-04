package sk.seges.sesam.pap.model.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class DefaultParametersResolver implements ParametersResolver {

	protected ProcessingEnvironment processingEnv;
	protected NameTypesUtils nameTypesUtils;
	
	public DefaultParametersResolver(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
	}
	
	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
			return new ParameterElement[0];
		}
		List<ParameterElement> variableElements = new ArrayList<ParameterElement>();

		DeclaredType declaredDomainType = (DeclaredType)domainType;
		
		if (declaredDomainType.getTypeArguments().size() > 0) {
			
			TypeElement dtoConverterType = processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName());
			
			int index = 0;
			for (TypeMirror typeParameterElement: declaredDomainType.getTypeArguments()) {

				if (typeParameterElement.getKind().equals(TypeKind.TYPEVAR)) {
					String typeParameterName = ((TypeVariable)typeParameterElement).asElement().getSimpleName().toString();
					
					if (typeParameterName != null && !typeParameterName.equals("?") && typeParameterName.length() > 0) {
						TypeParameter dtoTypeVariable = TypeParameterBuilder.get(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + typeParameterName);
						TypeParameter domainTypeVariable = TypeParameterBuilder.get(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + typeParameterName);
						
						HasTypeParameters dtoConverterNamedType = TypedClassBuilder.get(nameTypesUtils.toType(dtoConverterType), dtoTypeVariable, domainTypeVariable);
						
						ParameterElement converterManagerParameter = new ParameterElement(dtoConverterNamedType, ProcessorContext.LOCAL_CONVERTER_NAME + index, true);
						
						variableElements.add(converterManagerParameter);
					} else {
						//TODO handle type parameter with no variables
					}
				} else {
					//TODO handle type parameter with no variables
				}
			}
		}
		
		return variableElements.toArray(new ParameterElement[] {});
	}
}