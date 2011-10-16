package sk.seges.corpis.pap.model.printer.converter;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateConverterProviderPrinter extends ConverterProviderPrinter {

	public HibernateConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver) {
		super(pw, processingEnv, parametersResolver);
	}

	@Override
	protected MutableType[] getConverterParametersUsage(ConverterTypeElement converterTypeElement, MutableTypeMirror type, ExecutableElement method) {
		MutableType[] converterParameters = super.getConverterParametersUsage(converterTypeElement, type, method);
				
		TransactionPropagation transactionPropagation = method.getAnnotation(TransactionPropagation.class);
		Transactional transactional = method.getAnnotation(Transactional.class);
		
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableDeclaredType propagationType = typeUtils.toMutableType(PropagationType.class);
		MutableDeclaredType stringType = typeUtils.toMutableType(String.class);
		
		List<ConverterParameter> converterParametersTypes = converterTypeElement.getConverterParameters(parametersResolver);

		List<MutableType> generatedParams = new ArrayList<MutableType>();
		
		for (ConverterParameter converterParametersType: converterParametersTypes) {
		
			if (!converterParametersType.isPropagated()) {
				if (converterParametersType.getType().toString(ClassSerializer.CANONICAL, false).equals(propagationType.toString(ClassSerializer.CANONICAL, false))) {
					if (transactionPropagation != null) {
						generatedParams.add(typeUtils.getEnumValue(transactionPropagation.value()));
					} else if (transactional != null) {
						generatedParams.add(typeUtils.getEnumValue(PropagationType.PROPAGATE));
					} else {
						generatedParams.add(typeUtils.getEnumValue(PropagationType.ISOLATE));
					}
				} else if (converterParametersType.getType().toString(ClassSerializer.CANONICAL, false).equals(
							typeUtils.getArrayType(stringType).toString(ClassSerializer.CANONICAL, false))) {
					if (transactionPropagation != null) {
						generatedParams.add(typeUtils.getArrayValue(typeUtils.getArrayType(stringType), (Object[])transactionPropagation.fields()));
					} else if (transactional != null) {
						generatedParams.add(typeUtils.getArrayValue(typeUtils.getArrayType(stringType)));
					} else {
						generatedParams.add(typeUtils.getArrayValue(typeUtils.getArrayType(stringType)));
					}
				}
			}
		}

		MutableType[] result = new MutableType[converterParameters.length + generatedParams.size()];

		int i = 0;

		for (MutableType converterParameter: generatedParams) {
			result[i++] = converterParameter;
		}

		for (MutableType converterParameter: converterParameters) {
			result[i++] = converterParameter;
		}

		return result;
	}
}