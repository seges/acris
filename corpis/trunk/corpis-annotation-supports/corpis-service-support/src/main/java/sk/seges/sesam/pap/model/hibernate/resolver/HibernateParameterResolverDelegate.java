package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.persistence.EntityManager;

import sk.seges.corpis.pap.service.hibernate.accessor.TransactionPropagationAccessor;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageProvider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
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
		return new ParameterElement(processingEnv.getTypeUtils().getArrayType(processingEnv.getTypeUtils().toMutableType(TransactionPropagationModel.class)), 
			TRANSACTION_PROPAGATION_NAME, new ParameterUsageProvider() {
				
				@Override
				public MutableType getUsage(ParameterUsageContext context) {
					TransactionPropagationAccessor transactionPropagationAccessor = new TransactionPropagationAccessor(context.getMethod(), processingEnv);
					return getTransactionModelReference(transactionPropagationAccessor);
				}
			}, isTransactionPropagationModelParameterPropagated());
	}
	
	protected boolean isTransactionPropagationModelParameterPropagated() {
		return true;
	}

	protected MutableReferenceType getTransactionModelReference(TransactionPropagationAccessor transactionPropagationAccessor) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType transactionPropagationModel = typeUtils.toMutableType(TransactionPropagationModel.class);
		
		return processingEnv.getTypeUtils().getReference(typeUtils.getArrayValue(typeUtils.getArrayType(transactionPropagationModel), (Object[])transactionPropagationAccessor.getPropagations()),
				HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME);
	}

	protected MutableReferenceType getEntityManagerReference() {
		return null;
	}
	
	protected ParameterElement getEntityManagerModel() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(EntityManager.class),
				ENTITY_MANAGER_NAME, getEntityManagerReference(), true, processingEnv);
	}
	
	public ParameterElement[] getConstructorAditionalParameters(ParameterElement[] additionalConstructorParameters) {
		int additionalParams = 2;

		ParameterElement[] result = new ParameterElement[additionalConstructorParameters.length
				+ additionalParams];

		for (int i = 0; i < additionalConstructorParameters.length; i++) {
			result[i + additionalParams] = additionalConstructorParameters[i];
		}

		result[0] = getEntityManagerModel();
		result[1] = getTransactionPropagationModel();

		return result;
	}
}