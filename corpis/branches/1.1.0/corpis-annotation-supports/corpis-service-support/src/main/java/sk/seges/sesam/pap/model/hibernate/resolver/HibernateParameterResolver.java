package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;

public class HibernateParameterResolver extends DefaultParametersResolver {

	public static final String ENTITY_MANAGER_NAME = "entityManager";

	public HibernateParameterResolver(ProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		ParameterElement[] additionalConstructorParameters = super.getConstructorAditionalParameters(domainType);

		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length + 1];
		
		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + 1] = additionalConstructorParameters[i];
		}
		
		ParameterElement entityManagerParameter = new ParameterElement(new InputClass(EntityManager.class.getPackage().getName(), EntityManager.class.getSimpleName()), ENTITY_MANAGER_NAME, false);

		result[0] = entityManagerParameter;

		return result;
	}
	
}
