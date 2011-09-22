package sk.seges.sesam.pap.model.hibernate.printer.method;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateCopyFromDtoPrinter extends CopyFromDtoPrinter {

	public HibernateCopyFromDtoPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver,
			ParametersResolver parametersResolver, RoundEnvironment roundEnv,
			ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, entityResolver, parametersResolver, roundEnv, processingEnv, pw);
	}

	@Override
	protected void printDomainInstancer(FormattedPrintWriter pw, NamedType type) {
		pw.println("if (id != null) {");
		pw.println(type, " result = (", type, ")" + HibernateParameterResolver.ENTITY_MANAGER_NAME + ".find(", type.getSimpleName(), ".class, id);");
		pw.println("if (result != null) {");
		pw.println("return result;");
		pw.println("}");
		pw.println("}");
		pw.println();
		super.printDomainInstancer(pw, type);
	}
}