package sk.seges.sesam.pap.converter.printer.converterprovider;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public class ConverterProviderPrinterDelegate {

	private final FormattedPrintWriter pw;
	private final ConverterConstructorParametersResolverProvider parametersResolverProvider;

	public ConverterProviderPrinterDelegate(ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw) {
		this.pw = pw;
		this.parametersResolverProvider = parametersResolverProvider;
	}

	public void initialize(ConverterProviderType converterProviderType, UsageType usageType, Set<? extends Element> converterPrinterDelegates) {

		ParameterElement[] generatedParameters = converterProviderType.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType));

		for (ParameterElement generatedParameter : generatedParameters) {
			pw.println(Modifier.PROTECTED.toString() + " " + Modifier.FINAL.toString() + " ", generatedParameter.getType(), " " + generatedParameter.getName()
					+ ";");
			pw.println();
		}

		pw.print(Modifier.PUBLIC.toString() + " ", converterProviderType.getSimpleName() + "(");

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

		for (Element converterPrinterDelegate: converterPrinterDelegates) {
			pw.print("registerConverterProvider(new ", converterPrinterDelegate, "(");
			ConverterConstructorParametersResolver parameterResolver = parametersResolverProvider.getParameterResolver(UsageType.CONSTRUCTOR_CONVERTER_PROVIDER);
			ParameterElement[] converterParameters = converterProviderType.getConverterParameters(parameterResolver);
			
			int j = 0;
			for (ParameterElement converterParameter: converterParameters) {
				if (j > 0) {
					pw.print(", ");
				}
				pw.print(converterParameter.getName());
				j++;
			}
			pw.println("));");
		}
		
		pw.println("}");
		pw.println();
	}

	public void finalize() {
	}
}