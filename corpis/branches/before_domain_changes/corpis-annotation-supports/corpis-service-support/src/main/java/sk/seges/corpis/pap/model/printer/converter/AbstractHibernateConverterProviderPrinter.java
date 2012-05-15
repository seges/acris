package sk.seges.corpis.pap.model.printer.converter;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

import sk.seges.corpis.pap.service.hibernate.accessor.TransactionPropagationAccessor;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType.RenameActionType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public abstract class AbstractHibernateConverterProviderPrinter extends ConverterProviderPrinter {

	protected TransferObjectProcessingEnvironment processingEnv;
	
	public AbstractHibernateConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolver parametersResolver) {
		super(pw, processingEnv, parametersResolver);
		this.processingEnv = processingEnv;
	}

	protected MutableReferenceType getTransactionModelReference(TransactionPropagationAccessor transactionPropagationAccessor) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType transactionPropagationModel = typeUtils.toMutableType(TransactionPropagationModel.class);
		
		return processingEnv.getTypeUtils().getReference(typeUtils.getArrayValue(typeUtils.getArrayType(transactionPropagationModel), (Object[])transactionPropagationAccessor.getPropagations()),
				HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME);
	}
	
	protected abstract MutableReferenceType getConverterProviderReference();

	protected MutableReferenceType getCacheReference() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType cacheInstance = typeUtils.toMutableType(MapConvertedInstanceCache.class);
		
		return typeUtils.getReference(typeUtils.getTypeValue(cacheInstance, new MapConvertedInstanceCache()), ServiceConverterConstructorParametersResolver.CONVERTER_CACHE_NAME, true);
	}

	private ParameterElement toParameter(ConverterParameter converterParameter) {
		return new ParameterElement(converterParameter.getType(), converterParameter.getName(), converterParameter.isPropagated());
	}

	private ParameterElement[] toParameters(List<ConverterParameter> converterParameters) {
		ParameterElement[] result = new ParameterElement[converterParameters.size()];
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			result[i++] = toParameter(converterParameter);
		}
		return result;
	}
	
	@Override
	protected MutableType[] getConverterParametersUsage(ConverterTypeElement converterTypeElement, ExecutableElement method) {
		MutableType[] converterParameters = super.getConverterParametersUsage(converterTypeElement, method);
		
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableDeclaredType transactionPropagationModel = typeUtils.toMutableType(TransactionPropagationModel.class);
		
		TransactionPropagationAccessor transactionPropagationAccessor = new TransactionPropagationAccessor(method, processingEnv);
		
		ParameterElement[] converterParametersTypes = null;
		
		if (converterTypeElement == null) {
			converterParametersTypes = parametersResolver.getConstructorAditionalParameters();
		} else {
			converterParametersTypes = toParameters(converterTypeElement.getConverterParameters(parametersResolver));
		}
		
		List<MutableType> generatedParams = new ArrayList<MutableType>();

		//TODO - ugly implementation. Is it?
		//TODO move something to the sesam part
		for (ParameterElement converterParametersType: converterParametersTypes) {
			if (!converterParametersType.isPropagated()) {
				if (processingEnv.getTypeUtils().isSameType(converterParametersType.getType(), typeUtils.getArrayType(transactionPropagationModel))) {
					generatedParams.add(getTransactionModelReference(transactionPropagationAccessor));
				}
				if (processingEnv.getTypeUtils().isSameType(converterParametersType.getType(), typeUtils.toMutableType(ConverterProvider.class))) {
					generatedParams.add(getConverterProviderReference());
				}
				if (processingEnv.getTypeUtils().isSameType(converterParametersType.getType(), typeUtils.toMutableType(ConvertedInstanceCache.class))) {
					generatedParams.add(getCacheReference());
				}
			}
		}

		MutableType[] result = new MutableType[converterParameters.length + generatedParams.size()];

		int i = 0;

		for (MutableType converterParameter: generatedParams) {
			result[i++] = converterParameter;
		}

		for (MutableType converterParameter: converterParameters) {
			if (converterParameter instanceof MutableTypeMirror && ((MutableTypeMirror)converterParameter).getKind().isDeclared()) {
				result[i++] = ((MutableDeclaredType)converterParameter).clone().renameTypeParameter(RenameActionType.REPLACE, MutableWildcardType.WILDCARD_NAME);
			} else if (converterParameter instanceof MutableTypeMirror && ((MutableTypeMirror)converterParameter).getKind().equals(MutableTypeKind.TYPEVAR)) {
				if (((MutableTypeVariable)converterParameter).clone().getVariable() != null) {
					result[i++] = ((MutableTypeVariable)converterParameter).clone().setVariable(MutableWildcardType.WILDCARD_NAME);
				} else {
					result[i++] = converterParameter;
				}
			} else {
				result[i++] = converterParameter;
			}
		}

		return result;
	}

}
