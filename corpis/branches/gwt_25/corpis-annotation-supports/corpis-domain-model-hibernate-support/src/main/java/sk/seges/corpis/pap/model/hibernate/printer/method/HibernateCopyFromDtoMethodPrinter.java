package sk.seges.corpis.pap.model.hibernate.printer.method;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyFromDtoMethodPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.utils.CastUtils;

public class HibernateCopyFromDtoMethodPrinter extends CopyFromDtoMethodPrinter {

	private HibernateEntityResolver entityResolver;
	
	public HibernateCopyFromDtoMethodPrinter(Set<String> instances, ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(instances, converterProviderPrinter, entityResolver, parametersResolverProvider, roundEnv, processingEnv);
		this.entityResolver = (HibernateEntityResolver)entityResolver;
	}
	
	@Override
    protected void printCopyByConverter(ConverterTypeElement converter, ExecutableElement domainMethod, PathResolver domainPathResolver, String dtoField, FormattedPrintWriter pw) {
    	if (entityResolver.isLazyReference(domainMethod)) {
    		pw.println("if (", ConverterUtils.class,".convertArg(" + HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME + ", \"" + domainPathResolver.getPath() + "\")) {");
    		pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + " != null) {");
    		pw.println("if (" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField) + " != null) {");
    		String converterName = "converter" + MethodHelper.toMethod("", dtoField);
    		pw.print(converter.getConverterBase(), " " + converterName + " = ");
    		Field field = new Field(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField), converter.getDto());
    		converterProviderPrinter.printDtoEnsuredConverterMethodName(converter.getDto(), field, domainMethod, pw, false);
    		pw.println(";");
    		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");
    		pw.print("(", getDelegateCast(converter.getDomain(), true), ")");
    		pw.print(converterName + ".convertFromDto(");
    		
    		if (converter.getDomain().getKind().isDeclared() && ((DomainDeclaredType)converter.getDomain()).hasTypeParameters()) {
	    		pw.print(CastUtils.class, ".cast(");
	    		//pw.print("(", getDelegateCast(converter.getDomain()), ")");
	    		pw.print(TransferObjectElementPrinter.RESULT_NAME  + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ", ");
	    		pw.print(getTypeVariableDelegate(getDelegateCast(converter.getDomain(), true)), ".class), ");
    		} else {
	    		pw.print(TransferObjectElementPrinter.RESULT_NAME  + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ", ");
    		}

    		if (converter.getDto().getKind().isDeclared() && ((DtoDeclaredType)converter.getDto()).hasTypeParameters()) {
	    		pw.print(CastUtils.class, ".cast(");
	    		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField) + ", ");
	    		pw.print(getTypeVariableDelegate(getDelegateCast(converter.getDto(), true)), ".class)");
    		} else {
	    		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
    		}
    		
    		pw.println("));");
        	pw.println("} else {");
        	pw.println(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(null);");
        	pw.println("}");
        	pw.println("} else {");
    		super.printCopyByConverter(converter, domainMethod, domainPathResolver, dtoField, pw);
        	pw.println("}");
        	pw.println("}");
    	} else {
    		super.printCopyByConverter(converter, domainMethod, domainPathResolver, dtoField, pw);
    	}
    }    
}