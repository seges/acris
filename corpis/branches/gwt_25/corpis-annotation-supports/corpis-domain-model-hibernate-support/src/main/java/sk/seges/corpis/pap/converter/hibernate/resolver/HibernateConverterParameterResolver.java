package sk.seges.corpis.pap.converter.hibernate.resolver;

import javax.persistence.EntityManager;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;

public class HibernateConverterParameterResolver extends DefaultConverterConstructorParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateConverterParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {
			
			protected boolean isTransactionPropagationModelParameterPropagated() {
				return true;
			};

			@Override
			protected ParameterElement getEntityManagerModel() {
				return new ParameterElement(processingEnv.getTypeUtils().toMutableType(EntityManager.class), ENTITY_MANAGER_NAME, 
						null, true, processingEnv);
			}
		};
	}
	
//	@Override
//	protected MutableReferenceType getConverterProviderReference() {
//		return processingEnv.getTypeUtils().getReference(null, ConverterConstructorParametersResolver.THIS);
//	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(super.getConstructorAditionalParameters());
	}	
}