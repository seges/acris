package sk.seges.sesam.pap.model.printer.method;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.utils.CastUtils;

public class CopyToDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private ElementHolderTypeConverter elementHolderTypeConverter;
	
	public CopyToDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, 
			ConverterConstructorParametersResolver parametersResolver, EntityResolver entityResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolver, entityResolver, roundEnv, processingEnv);
		this.elementHolderTypeConverter = elementHolderTypeConverter;
	}

	@Override
	public void printCopyMethod(TransferObjectContext context, FormattedPrintWriter pw) {

		if (isIdField(context.getConfigurationTypeElement().getInstantiableDomain(), context.getDomainMethod().getSimpleName().toString())) {
			return;
		}

		PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

		boolean nested = pathResolver.isNested();
		
		if (nested) {
			pw.print("if (");
			int i = 0;
			
			String methodPath = TransferObjectElementPrinter.DOMAIN_NAME;
			
			while (pathResolver.hasNext()) {
				
				String path = pathResolver.next();
				
				//TODO check if collection is not nested type - you could not use this notation on the collections (and also it can be used only on declared types)
				if (pathResolver.hasNext()) {
					if (i > 0) {
						pw.print(" && ");
					}
					methodPath += "." + MethodHelper.toGetter(path);
					
					pw.print(methodPath + " != null");
					i++;
				}
			}
			pw.println(") {");
		} 

	    boolean lazy = printInitialized(context.getDomainMethod(), pathResolver, pw);

//		tuto treba
//		pw.println("if (isInitialized(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName() + ")) {");
		
		if (context.getConverter() != null) {
						
			String converterName = "converter" + MethodHelper.toMethod("", context.getDtoFieldName());
			
			pw.print(context.getConverter().getConverterBase(), " " + converterName + " = ");
			converterProviderPrinter.printDomainEnsuredConverterMethodName(context.getConverter().getDomain(), TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName(), null, pw, false);
			pw.println(";");

			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDtoFieldName()) + "(");

			pw.print(converterName + ".toDto(");
			pw.print(CastUtils.class, ".cast(");
			MutableTypeMirror delegateCast = getDelegateCast(context.getConverter().getDomain());
			pw.print("(", getWildcardDelegate(delegateCast), ")");
			pw.print(TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
			pw.print(", ", getTypeVariableDelegate(delegateCast), ".class)");

		} else if (context.isLocalConverter()) {
			String converterName = printLocalConverter(context, ConverterTargetType.DOMAIN, pw);
			pw.println("if (" + converterName + " != null) {");
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDtoFieldName()) + "(" + 
					converterName + ".toDto(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName() + ")");
		} else {
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDtoFieldName()) + "(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
		}
		
		if (context.getConverter() != null) {
			//Creating iterable types - this should be overridden if you want to create LinkedList instead of ArrayList, etc.
			//If you do specify any type, original type we be taken, but this can lead for using not supported types in the DTOs - like PersistenceBag instead of List
			TypeMirror listType = elementHolderTypeConverter.getIterableDtoType(context.getDomainMethodReturnType());
			
			if (listType != null) {
				pw.print(", " + processingEnv.getTypeUtils().toMutableType(processingEnv.getTypeUtils().erasure(listType)).toString(ClassSerializer.CANONICAL, false) + ".class)");
			} else {
				pw.print(")");
			}
		}
		
		pw.println(");");
		
		if (context.isLocalConverter()) {
			printCopyByLocalConverter(context.getDomainFieldName(), context.getDomainMethodReturnType(), context.getDtoFieldName(), pw);
		}
		
		if (lazy) {
			pw.println("};");
		}
		
		if (nested) {
			pw.println("} else {");
			pw.println(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDtoFieldName()) + "(null);");
			pw.println("}");
			pw.println("");
		}
	}
	
    protected boolean printInitialized(ExecutableElement domainMethod, PathResolver domainPathResolver, FormattedPrintWriter pw) { 
    	return false;
    }

	protected void printCopyByLocalConverter(String domainField, DomainType domainMethodReturnType, String dtoField, FormattedPrintWriter pw) {
		pw.println("} else {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(dtoField));
		if (domainMethodReturnType.getKind().equals(MutableTypeKind.TYPEVAR)) {
			pw.print("((" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + ((MutableTypeVariable)domainMethodReturnType).getVariable() + ")");
		} else {
			pw.print("((" + dtoField + ")");
		}
		pw.print(TransferObjectElementPrinter.DOMAIN_NAME  + "." + domainField);
		pw.println(");");
		pw.println("}");
	}
}