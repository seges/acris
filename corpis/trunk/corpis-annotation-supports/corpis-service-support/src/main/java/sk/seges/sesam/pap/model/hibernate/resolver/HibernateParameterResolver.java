package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;

public class HibernateParameterResolver extends DefaultParametersResolver {

	public static final String ENTITY_MANAGER_NAME = "entityManager";

	public HibernateParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		ParameterElement[] additionalConstructorParameters = super.getConstructorAditionalParameters(domainType);

		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length + 1];
		
		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + 1] = additionalConstructorParameters[i];
		}
		
		ParameterElement entityManagerParameter = new ParameterElement(
				processingEnv.getTypeUtils().toMutableType(EntityManager.class), ENTITY_MANAGER_NAME, false);

		result[0] = entityManagerParameter;

		return result;
	}
	
}
