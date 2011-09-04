package sk.seges.sesam.pap.model.printer.accessors;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class AccessorsPrinter extends AbstractElementPrinter {

	private MethodHelper methodHelper;
	
	public AccessorsPrinter(ProcessingEnvironment processingEnv, PrintWriter pw) {
		super(pw);
		this.methodHelper = new MethodHelper(processingEnv, new NameTypesUtils(processingEnv));
	}
	
	@Override
	public void print(ProcessorContext context) {

		String fieldTypeName = context.getFieldType().toString(ClassSerializer.CANONICAL, true);
		
		String modifier = Modifier.PUBLIC.toString() + " ";
		
		//modifier = context.getModifier() != null ? (context.getModifier().toString() + " ") : "";
		
		pw.println(modifier + fieldTypeName + " " + methodHelper.toGetter(context.getFieldName()) + " {");
		pw.println("return " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();

		pw.println(modifier + "void " + methodHelper.toSetter(context.getFieldName()) + 
				"(" + fieldTypeName + " " + context.getFieldName() + ") {");
		pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();
	}
}