package sk.seges.corpis.appscaffold.model.pap.printer;

import sk.seges.corpis.appscaffold.model.pap.model.DataConfigurationTypeElement;
import sk.seges.corpis.pap.service.hibernate.printer.HibernateServiceMethodConverterPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class HibernateDataServiceMethodConverterPrinter extends HibernateServiceMethodConverterPrinter {

	public HibernateDataServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver, FormattedPrintWriter pw,
			ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	protected void printCastLocalMethodResult(DtoType returnDtoType, ServiceConverterPrinterContext context) {
		if (returnDtoType != null) {
			ConfigurationTypeElement configuration = returnDtoType.getDomainDefinitionConfiguration();
		
			if (configuration instanceof DataConfigurationTypeElement) {
				pw.print("(", ((DataConfigurationTypeElement)configuration).getInstantiableDomain(), ")");
			}
		}
	}
}
