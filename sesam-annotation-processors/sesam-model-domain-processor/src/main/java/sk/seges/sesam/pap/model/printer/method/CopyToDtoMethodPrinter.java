package sk.seges.sesam.pap.model.printer.method;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyToDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private static final String RESULT_NAME = "_result";
	private static final String DOMAIN_NAME = "_domain";

	private ElementHolderTypeConverter elementHolderTypeConverter;
	
	public CopyToDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, EntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.elementHolderTypeConverter = elementHolderTypeConverter;
	}

	@Override
	public void printCopyMethod(ProcessorContext context, FormattedPrintWriter pw) {

		PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

		boolean nested = pathResolver.isNested();
		
		if (nested) {
			pw.print("if (");
			int i = 0;
			
			String methodPath = DOMAIN_NAME;
			
			while (pathResolver.hasNext()) {
				
				String path = pathResolver.next();
				
				//TODO check if collection is not nested type - you could not use this notation on the collections (and also it can be used only on declared types)
				if (pathResolver.hasNext()) {
					if (i > 0) {
						pw.print(" && ");
					}
					methodPath += "." + methodHelper.toGetter(path);
					
					pw.print(methodPath + " != null");
					i++;
				}
			}
			pw.println(") {");
		} 

		pw.println("if (isInitialized(" + DOMAIN_NAME  + "." + context.getDomainFieldName() + ")) {");
		
		if (context.getConverterType() != null) {
			
			String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
			
			pw.print(context.getConverterType().toString(ClassSerializer.CANONICAL, true) + " " + converterName + " = ");
			converterProviderPrinter.printDomainConverterMethodName(context.getConverterType(), context.getDomainMethod().getReturnType(), pw);
			pw.println(";");

			pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(");

			pw.print(converterName + ".toDto(");
			pw.print("(" + castToDelegate(context.getDomainMethodReturnType()).toString(ClassSerializer.CANONICAL, true) + ")");

			pw.print(DOMAIN_NAME  + "." + context.getDomainFieldName());
		} else if (context.getLocalConverterName() != null) {
			pw.println("if (" + context.getLocalConverterName() + " != null) {");
			pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(" + 
					context.getLocalConverterName() + ".toDto(" + DOMAIN_NAME  + "." + context.getDomainFieldName() + ")");
		} else {
			pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(" + DOMAIN_NAME  + "." + context.getDomainFieldName());
		}
		
		if (context.getConverterType() != null) {
			//Creating iterable types - this should be overridden if you want to create LinkedList instead of ArrayList, etc.
			//If you do specify any type, original type we be taken, but this can lead for using not supported types in the DTOs - like PersistenceBag instead of List
			TypeMirror listType = elementHolderTypeConverter.getIterableDtoType(context.getDomainMethodReturnType());
			
			if (listType != null) {
				pw.print(", " + nameTypesUtils.toType(processingEnv.getTypeUtils().erasure(listType)).toString(ClassSerializer.CANONICAL, false) + ".class)");
			} else {
				pw.print(")");
			}
		}
		
		pw.println(");");
		
		if (context.getLocalConverterName() != null) {
			pw.println("} else {");
			pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()));
			if (context.getDomainMethodReturnType().getKind().equals(TypeKind.TYPEVAR)) {
				pw.print("((" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + ((TypeVariable)context.getDomainMethodReturnType()).asElement().getSimpleName().toString() + ")");
			} else {
				pw.print("((" + context.getFieldType() + ")");
			}
			pw.print(DOMAIN_NAME  + "." + context.getDomainFieldName());
			pw.println(");");
			pw.println("}");
		}
		
		pw.println("};");
		
		if (nested) {
			pw.println("} else {");
			pw.println(RESULT_NAME + "." + methodHelper.toSetter(context.getFieldName()) + "(null);");
			pw.println("}");
			pw.println("");
		}
	}	
}