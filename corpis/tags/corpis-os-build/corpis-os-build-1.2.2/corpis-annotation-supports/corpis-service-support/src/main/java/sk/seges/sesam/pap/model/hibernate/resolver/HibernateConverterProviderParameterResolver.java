package sk.seges.sesam.pap.model.hibernate.resolver;

import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;

public class HibernateConverterProviderParameterResolver extends ServiceConverterConstructorParametersResolver {

	private final HibernateParameterResolverDelegate hibernateParameterResolverDelegate;

	public HibernateConverterProviderParameterResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.hibernateParameterResolverDelegate = new HibernateParameterResolverDelegate(processingEnv) {
			protected ParameterElement getTransactionPropagationModel() {
				return new ParameterElement(processingEnv.getTypeUtils().getArrayType(processingEnv.getTypeUtils().toMutableType(TransactionPropagationModel.class)), TRANSACTION_PROPAGATION_NAME, false);
			}
		};
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
		return hibernateParameterResolverDelegate.getConstructorAditionalParameters(super.getConstructorAditionalParameters());
	}	
	
	@Override
	protected ParameterElement getConverterCacheParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class), ConverterProviderPrinter.CONVERTER_CACHE_NAME,  true);
	}

}