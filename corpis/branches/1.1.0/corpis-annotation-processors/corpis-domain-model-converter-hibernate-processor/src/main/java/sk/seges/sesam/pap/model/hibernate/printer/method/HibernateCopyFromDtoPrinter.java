package sk.seges.sesam.pap.model.hibernate.printer.method;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoPrinter;
import sk.seges.sesam.pap.model.resolver.api.IdentityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateCopyFromDtoPrinter extends CopyFromDtoPrinter {

	public HibernateCopyFromDtoPrinter(ConverterProviderPrinter converterProviderPrinter, IdentityResolver identityResolver,
			ParametersResolver parametersResolver, RoundEnvironment roundEnv,
			ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, identityResolver, parametersResolver, roundEnv, processingEnv, pw);
	}

	@Override
	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("if (id != null) {");
		pw.println(type.toString(ClassSerializer.SIMPLE, true) + " result = (" + type.toString(ClassSerializer.SIMPLE, true) + ")" + 
				HibernateParameterResolver.ENTITY_MANAGER_NAME + ".find(" + type.getSimpleName() + ".class, id);");
		pw.println("if (result != null) {");
		pw.println("return result;");
		pw.println("}");
		pw.println("}");
		pw.println();
		super.printDomainInstancer(pw, type);
	}
}