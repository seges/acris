package sk.seges.sesam.pap.converter.printer.converterprovider;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ConstructorParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.model.HasConstructorParameters;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;

public class ConverterProviderPrinterDelegate {

	protected final FormattedPrintWriter pw;
	protected final ConverterConstructorParametersResolverProvider parametersResolverProvider;

	public ConverterProviderPrinterDelegate(ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw) {
		this.pw = pw;
		this.parametersResolverProvider = parametersResolverProvider;
	}

	public void initialize(MutableTypes typeUtils, HasConstructorParameters type, UsageType usageType) {

		ParameterElement[] generatedParameters = type.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType));

		for (ParameterElement generatedParameter : generatedParameters) {
			pw.println(Modifier.PROTECTED.toString() + " " + Modifier.FINAL.toString() + " ", generatedParameter.getType(), " " + generatedParameter.getName()
					+ ";");
			pw.println();
		}

		pw.print(Modifier.PUBLIC.toString() + " ", type.getSimpleName() + "(");

		int i = 0;
		for (ParameterElement generatedParameter : generatedParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(generatedParameter.getType(), " " + generatedParameter.getName());
			i++;
		}

		pw.println(") {");

		for (ParameterElement generatedParameter : generatedParameters) {
			pw.println("this." + generatedParameter.getName() + " = " + generatedParameter.getName() + ";");
		}

		printConstructorBody();
		
		pw.println("}");
		pw.println();
	}

	protected void printConstructorBody() {}
	
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