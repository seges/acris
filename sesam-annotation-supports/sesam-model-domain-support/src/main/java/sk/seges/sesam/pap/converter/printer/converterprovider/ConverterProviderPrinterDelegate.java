package sk.seges.sesam.pap.converter.printer.converterprovider;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;

public class ConverterProviderPrinterDelegate {

	private final FormattedPrintWriter pw;
	private final ConverterConstructorParametersResolverProvider parametersResolverProvider;

	public ConverterProviderPrinterDelegate(ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw) {
		this.pw = pw;
		this.parametersResolverProvider = parametersResolverProvider;
	}

	public void initialize(ConverterProviderType converterProviderType, UsageType usageType) {

		ParameterElement[] generatedParameters = converterProviderType.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType));

		for (ParameterElement generatedParameter : generatedParameters) {
			pw.println(Modifier.PROTECTED.toString() + " " + Modifier.FINAL.toString() + " ", generatedParameter.getType(), " " + generatedParameter.getName()
					+ ";");
			pw.println();
		}

		pw.print(Modifier.PROTECTED.toString() + " ", converterProviderType.getSimpleName() + "(");

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

		pw.println("}");
		pw.println();
	}

	public void finalize() {
	}
}