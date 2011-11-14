package sk.seges.sesam.pap.model.hibernate.printer.method;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;

import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoMethodPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateCopyFromDtoMethodPrinter extends CopyFromDtoMethodPrinter {

	private HibernateEntityResolver entityResolver;
	
	public HibernateCopyFromDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver,
			ParametersResolver parametersResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, entityResolver, parametersResolver, roundEnv, processingEnv);
		this.entityResolver = (HibernateEntityResolver)entityResolver;
	}
	@Override
    protected void printCopyByConverter(ConverterTypeElement converter, ExecutableElement domainMethod, PathResolver domainPathResolver, DomainType domainMethodReturnType, String dtoField, FormattedPrintWriter pw) {
    	if (entityResolver.isLazyReference(domainMethod)) {
    		pw.println("if (", ConverterUtils.class,".convertArg(" + HibernateParameterResolver.TRANSACTION_PROPAGATION_NAME + ", " + 
    				HibernateParameterResolver.TRANSACTION_PROPAGATION_NAME + ", \"" + domainPathResolver.getPath() + "\")) {");
        	//pw.println("if (" + HibernateParameterResolver.PROPAGATION_TYPE_NAME + ".equals(", PropagationType.class, ".", PropagationType.PROPAGATE, ")) {");
    		pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + " != null) {");
    		pw.println("if (" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField) + " != null) {");
    		String converterName = "converter" + MethodHelper.toMethod("", dtoField);
    		pw.print(converter, " " + converterName + " = ");
    		converterProviderPrinter.printDomainConverterMethodName(converter, domainMethodReturnType, domainMethod, pw);
    		pw.println(";");
    		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");
    		pw.print("(", castToDelegate(domainMethodReturnType), ")");
    		pw.print(converterName + ".convertFromDto(");
    		pw.print(TransferObjectElementPrinter.RESULT_NAME  + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ",");
    		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
    		pw.println("));");
        	pw.println("} else {");
        	pw.println(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(null);");
        	pw.println("}");
        	pw.println("} else {");
    		super.printCopyByConverter(converter, domainMethod, domainPathResolver, domainMethodReturnType, dtoField, pw);
        	pw.println("}");
        	pw.println("}");
    	} else {
    		super.printCopyByConverter(converter, domainMethod, domainPathResolver, domainMethodReturnType, dtoField, pw);
    	}
    }    
}