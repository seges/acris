package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.persistence.EntityManager;

import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;

public class HibernateParameterResolverDelegate {

	public static final String ENTITY_MANAGER_NAME = "entityManager";
	public static final String TRANSACTION_PROPAGATION_NAME = "transactionPropagations";

	protected final MutableProcessingEnvironment processingEnv;
	
	public HibernateParameterResolverDelegate(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	protected ParameterElement getTransactionPropagationModel() {
		return new ParameterElement(processingEnv.getTypeUtils().getArrayType(processingEnv.getTypeUtils().toMutableType(TransactionPropagationModel.class)), TRANSACTION_PROPAGATION_NAME, true);
	}
	
	public ParameterElement[] getConstructorAditionalParameters(ParameterElement[] additionalConstructorParameters) {
		int additionalParams = 2;

		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length
				+ additionalParams];

		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + additionalParams] = additionalConstructorParameters[i];
		}

		MutableTypes typeUtils = processingEnv.getTypeUtils();

		result[0] = new ParameterElement(typeUtils.toMutableType(EntityManager.class),ENTITY_MANAGER_NAME, true);
		result[1] = getTransactionPropagationModel();

		return result;
	}
}