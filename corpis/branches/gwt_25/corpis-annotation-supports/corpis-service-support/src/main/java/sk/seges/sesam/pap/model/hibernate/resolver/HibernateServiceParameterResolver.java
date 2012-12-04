package sk.seges.sesam.pap.model.hibernate.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

public class HibernateServiceParameterResolver extends ServiceConverterConstructorParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateServiceParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {

			protected boolean isTransactionPropagationModelParameterPropagated() {
				return false;
			};
		};
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(super.getConstructorAditionalParameters());
	}	
}