package sk.seges.corpis.pap.model.hibernate.printer.method;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectMappingAccessor;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
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
    protected void printCopyByConverter(TransferObjectContext context, PathResolver domainPathResolver, FormattedPrintWriter pw) {
    	if (entityResolver.isLazyReference(context.getDomainMethod())) {
    		pw.println("if (", ConverterUtils.class,".convertArg(" + HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME + ", \"" + domainPathResolver.getPath() + "\")) {");
    		pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + " != null) {");
    		pw.println("if (" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()) + " != null) {");
    		String converterName = "converter" + MethodHelper.toMethod("", context.getDtoFieldName());
    		pw.print(context.getConverter().getConverterBase(), " " + converterName + " = ");
    		Field field = new Field(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()), context.getConverter().getDto());
    		//converterProviderPrinter.printDtoEnsuredConverterMethodName(converter.getDto(), field, domainMethod, pw, false);
    		
    		TransferObjectMappingAccessor transferObjectMappingAccessor = new TransferObjectMappingAccessor(context.getDtoMethod(), processingEnv);
    		if (transferObjectMappingAccessor.isValid() && transferObjectMappingAccessor.getConverter() != null) {
//    			converterProviderPrinter.printDtoEnsuredConverterMethodName(converter.getDto(), field, dtoMethod, pw, false);
    			converterProviderPrinter.printDtoGetConverterMethodName(context.getConverter().getDto(), field, context.getDtoMethod(), pw, false);
    		} else {
    			converterProviderPrinter.printObtainConverterFromCache(ConverterTargetType.DTO, context.getConverter().getDomain(), field, context.getDomainMethod(), true);
    		}

    		
    		pw.println(";");
    		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");
    		pw.print("(", getDelegateCast(context.getConverter().getDomain(), true), ")");
    		pw.print(converterName + ".convertFromDto(");
    		
    		if (context.getConverter().getDomain().getKind().isDeclared() && ((DomainDeclaredType)context.getConverter().getDomain()).hasTypeParameters()) {
	    		pw.print(CastUtils.class, ".cast(");
	    		//pw.print("(", getDelegateCast(converter.getDomain()), ")");
	    		pw.print(TransferObjectElementPrinter.RESULT_NAME  + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ", ");
	    		pw.print(getTypeVariableDelegate(getDelegateCast(context.getConverter().getDomain(), true)), ".class), ");
    		} else {
	    		pw.print(TransferObjectElementPrinter.RESULT_NAME  + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ", ");
    		}

    		if (context.getConverter().getDto().getKind().isDeclared() && ((DtoDeclaredType)context.getConverter().getDto()).hasTypeParameters()) {
	    		pw.print(CastUtils.class, ".cast(");
	    		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()) + ", ");
	    		pw.print(getTypeVariableDelegate(getDelegateCast(context.getConverter().getDto(), true)), ".class)");
    		} else {
	    		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getDtoFieldName()));
    		}
    		
    		pw.println("));");
        	pw.println("} else {");
        	pw.println(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(null);");
        	pw.println("}");
        	pw.println("} else {");
    		super.printCopyByConverter(context, domainPathResolver, pw);
        	pw.println("}");
        	pw.println("}");
    	} else {
    		super.printCopyByConverter(context, domainPathResolver, pw);
    	}
    }    
}