package sk.seges.sesam.pap.model.hibernate.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

public class HibernateConverterProviderParameterResolver extends ServiceConverterConstructorParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateConverterProviderParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {

			@Override
			protected boolean isTransactionPropagationModelParameterPropagated() {
				return false;
			}
		};
	}

//	@Override
//	protected MutableReferenceType getConverterProviderReference() {
//		return processingEnv.getTypeUtils().getReference(null, THIS);
//	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(super.getConstructorAditionalParameters());
	}	
	
	@Override
	protected boolean isConverterCacheParameterPropagated() {
		return false;
	}
}