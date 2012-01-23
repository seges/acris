package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;

public class HibernateParameterResolver extends DefaultParametersResolver {

	public static final String ENTITY_MANAGER_NAME = "entityManager";
	public static final String TRANSACTION_PROPAGATION_NAME = "transactionPropagations";

	public HibernateParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		ParameterElement[] additionalConstructorParameters = super.getConstructorAditionalParameters(domainType);

		int additionalParams = 2;
		
		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length + additionalParams];
		
		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + additionalParams] = additionalConstructorParameters[i];
		}
		
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		result[0] = new ParameterElement(typeUtils.toMutableType(EntityManager.class), ENTITY_MANAGER_NAME, true);
		result[1] = new ParameterElement(typeUtils.getArrayType(typeUtils.toMutableType(TransactionPropagationModel.class)), TRANSACTION_PROPAGATION_NAME, true);

		return result;
	}	
}