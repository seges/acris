package sk.seges.sesam.pap.model.printer.method;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyToDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private ElementHolderTypeConverter elementHolderTypeConverter;
	
	public CopyToDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, EntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.elementHolderTypeConverter = elementHolderTypeConverter;
	}

	@Override
	public void printCopyMethod(TransferObjectContext context, FormattedPrintWriter pw) {

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

		pw.println("if (isInitialized(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName() + ")) {");
		
		if (context.getConverter() != null) {
			
			String converterName = "converter" + MethodHelper.toMethod("", context.getFieldName());
			
			pw.print(context.getConverter(), " " + converterName + " = ");
			converterProviderPrinter.printDomainConverterMethodName(context.getConverter(), processingEnv.getTypeUtils().toMutableType(context.getDomainMethod().getReturnType()), pw);
			pw.println(";");

			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getFieldName()) + "(");

			pw.print(converterName + ".toDto(");
			pw.print("(", castToDelegate(context.getDomainMethodReturnType()), ")");

			pw.print(TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
		} else if (context.getLocalConverterName() != null) {
			pw.println("if (" + context.getLocalConverterName() + " != null) {");
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getFieldName()) + "(" + 
					context.getLocalConverterName() + ".toDto(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName() + ")");
		} else {
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getFieldName()) + "(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
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
		
		if (context.getLocalConverterName() != null) {
			pw.println("} else {");
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getFieldName()));
			if (context.getDomainMethodReturnType().getKind().equals(TypeKind.TYPEVAR)) {
				pw.print("((" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + ((TypeVariable)context.getDomainMethodReturnType()).asElement().getSimpleName().toString() + ")");
			} else {
				pw.print("((" + context.getFieldType() + ")");
			}
			pw.print(TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
			pw.println(");");
			pw.println("}");
		}
		
		pw.println("};");
		
		if (nested) {
			pw.println("} else {");
			pw.println(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getFieldName()) + "(null);");
			pw.println("}");
			pw.println("");
		}
	}	
}