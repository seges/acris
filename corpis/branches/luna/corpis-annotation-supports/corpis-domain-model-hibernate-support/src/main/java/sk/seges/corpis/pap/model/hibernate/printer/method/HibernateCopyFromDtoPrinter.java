	package sk.seges.corpis.pap.model.hibernate.printer.method;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class HibernateCopyFromDtoPrinter extends CopyFromDtoPrinter {
	
	public HibernateCopyFromDtoPrinter(Set<String> nestedInstances, ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver,
			ConverterConstructorParametersResolver parametersResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw) {
		super(nestedInstances, converterProviderPrinter, entityResolver, parametersResolver, roundEnv, processingEnv, pw);
	}

	@Override
	public void print(TransferObjectContext context) {
		copy(context, pw, new HibernateCopyFromDtoMethodPrinter(nestedInstances, converterProviderPrinter, entityResolver, parametersResolver, roundEnv, processingEnv));
	}
	
	@Override
	protected void printDomainInstancer(FormattedPrintWriter pw, DomainDeclaredType domainType) {
		pw.println("if (id != null) {");
		pw.println(domainType, " " + RESULT_NAME + " = (", domainType, ")" + HibernateParameterResolverDelegate.ENTITY_MANAGER_NAME + ".find(", domainType.getSimpleName(), ".class, id);");
		pw.println("if (" + RESULT_NAME + " != null) {");
		pw.println("return " + RESULT_NAME + ";");
		pw.println("}");
		pw.println("}");
		pw.println();
		super.printDomainInstancer(pw, domainType);
	}
}