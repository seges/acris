package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.resolver.ServiceParametersResolver;

public class HibernateServiceParameterResolver extends ServiceParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateServiceParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {
			protected ParameterElement getTransactionPropagationModel() {
				return new ParameterElement(processingEnv.getTypeUtils().getArrayType(processingEnv.getTypeUtils().toMutableType(TransactionPropagationModel.class)), TRANSACTION_PROPAGATION_NAME, false);
			}
		};
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType) {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(domainType, super.getConstructorAditionalParameters(domainType));
	}	
}