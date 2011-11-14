package sk.seges.corpis.pap.model.printer.converter;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

import sk.seges.corpis.pap.service.hibernate.accessor.TransactionPropagationAccessor;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
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

public class HibernateServiceConverterProviderPrinter extends ConverterProviderPrinter {

	private TransferObjectProcessingEnvironment processingEnv;
	
	public HibernateServiceConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver) {
		super(pw, processingEnv, parametersResolver);
		this.processingEnv = processingEnv;
	}

	@Override
	protected MutableType[] getConverterParametersUsage(ConverterTypeElement converterTypeElement, MutableTypeMirror type, ExecutableElement method) {
		MutableType[] converterParameters = super.getConverterParametersUsage(converterTypeElement, type, method);
				
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableDeclaredType transactionPropagationModel = typeUtils.toMutableType(TransactionPropagationModel.class);
		
		TransactionPropagationAccessor transactionPropagationAccessor = new TransactionPropagationAccessor(method, processingEnv);
		
		List<ConverterParameter> converterParametersTypes = converterTypeElement.getConverterParameters(parametersResolver);

		List<MutableType> generatedParams = new ArrayList<MutableType>();
		
		for (ConverterParameter converterParametersType: converterParametersTypes) {
			if (!converterParametersType.isPropagated()) {
				if (processingEnv.getTypeUtils().isSameType(converterParametersType.getType(), typeUtils.getArrayType(transactionPropagationModel))) {
					generatedParams.add(typeUtils.getArrayValue(typeUtils.getArrayType(transactionPropagationModel), (Object[])transactionPropagationAccessor.getPropagations()));
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
	
	@Override
	protected void printConverterParametersDefinition(List<ConverterParameter> converterParameters, ConverterTypeElement converterTypeElement) {
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
			if (converterParameter.isConverter() || !converterParameter.isPropagated()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				if (converterParameter.isConverter()) {
					MutableDeclaredType parameterReplacedTypeParameters = ((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(converterParameter.getType())).setTypeVariables(toTypeParameters(converterTypeElement, false));
					pw.print(parameterReplacedTypeParameters, " " + converterParameter.getName());
				} else {
					pw.print(converterParameter.getType(), " " + converterParameter.getName());
				}
				i++;
			}
		}
	}

}