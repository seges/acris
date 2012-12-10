package sk.seges.sesam.pap.model.hibernate.resolver;

import javax.persistence.EntityManager;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

public class HibernateServiceParameterResolver extends ServiceConverterConstructorParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateServiceParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {

			@Override
			protected boolean isTransactionPropagationModelParameterPropagated() {
				return false;
			};
			
			@Override
			protected ParameterElement getEntityManagerModel() {
				return new ParameterElement(processingEnv.getTypeUtils().toMutableType(EntityManager.class),
						ENTITY_MANAGER_NAME, getEntityManagerReference(), isEntityManagerPropagated(), processingEnv);
			}
		};
	}

	public boolean isEntityManagerPropagated() {
		return true;
	}
	
	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(super.getConstructorAditionalParameters());
	}	
}