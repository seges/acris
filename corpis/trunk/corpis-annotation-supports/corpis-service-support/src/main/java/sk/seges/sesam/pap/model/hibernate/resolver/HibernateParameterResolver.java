package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;

public class HibernateParameterResolver extends DefaultParametersResolver {

	public static final String ENTITY_MANAGER_NAME = "entityManager";
	public static final String PROPAGATION_TYPE_NAME = "propagationType";
	public static final String FIELDS_NAME = "fields";

	public HibernateParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		ParameterElement[] additionalConstructorParameters = super.getConstructorAditionalParameters(domainType);

		int additionalParams = 3;
		
		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length + additionalParams];
		
		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + additionalParams] = additionalConstructorParameters[i];
		}
		
		result[0] = new ParameterElement(processingEnv.getTypeUtils().toMutableType(EntityManager.class), ENTITY_MANAGER_NAME, true);
		result[1] = new ParameterElement(processingEnv.getTypeUtils().toMutableType(PropagationType.class), PROPAGATION_TYPE_NAME, false);
		result[2] = new ParameterElement(processingEnv.getTypeUtils().getArrayType(processingEnv.getTypeUtils().toMutableType(String.class)), FIELDS_NAME, false);

		return result;
	}
	
}