package sk.seges.sesam.pap.converter.printer.converterprovider;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ConstructorParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.pap.converter.model.HasConstructorParameters;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;

public class ConverterProviderPrinterDelegate {

	protected final ConverterConstructorParametersResolverProvider parametersResolverProvider;

	public ConverterProviderPrinterDelegate(ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		this.parametersResolverProvider = parametersResolverProvider;
	}

	public void initialize(MutableProcessingEnvironment processingEnv, HasConstructorParameters type, UsageType usageType) {

		ParameterElement[] generatedParameters = type.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType));

		HierarchyPrintWriter printWriter = type.getConstructor().addModifier(Modifier.PUBLIC).getPrintWriter();
		
		for (ParameterElement generatedParameter : generatedParameters) {
			MutableVariableElement field = processingEnv.getElementUtils().getParameterElement(generatedParameter.getType(), generatedParameter.getName());
			type.addField((MutableVariableElement) field.addModifier(Modifier.PROTECTED).addModifier(Modifier.FINAL));
			field = processingEnv.getElementUtils().getParameterElement(generatedParameter.getType(), generatedParameter.getName());
			type.getConstructor().addParameter(field);
			printWriter.println("this." + generatedParameter.getName() + " = " + generatedParameter.getName() + ";");
		}

		printConstructorBody(printWriter);
	}

	protected void printConstructorBody(HierarchyPrintWriter printWriter) {}
	
	protected ParameterElement getParameterElementByType(ConstructorParameter constructorParameter, ParameterElement[] converterParameters) {
		if (converterParameters == null) {
			return null;
		}
		
		for (int i = 0; i < converterParameters.length; i++) {
			if (constructorParameter.getType().toString().equals(converterParameters[i].getType().toString())) {
				return converterParameters[i];
			}
		}
		
		return null;
	}
	
	public void finalize() {
	}
}