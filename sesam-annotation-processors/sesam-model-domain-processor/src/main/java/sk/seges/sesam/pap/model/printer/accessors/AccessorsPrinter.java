package sk.seges.sesam.pap.model.printer.accessors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class AccessorsPrinter extends AbstractElementPrinter {

	private MethodHelper methodHelper;
	
	public AccessorsPrinter(ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(pw);
		this.methodHelper = new MethodHelper(processingEnv, new NameTypeUtils(processingEnv));
	}
	
	@Override
	public void print(ProcessorContext context) {

		String modifier = Modifier.PUBLIC.toString() + " ";
		
		//modifier = context.getModifier() != null ? (context.getModifier().toString() + " ") : "";
		
		pw.println(modifier, context.getFieldType(), " " + methodHelper.toGetter(context.getFieldName()) + " {");
		pw.println("return " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();

		pw.println(modifier + "void " + methodHelper.toSetter(context.getFieldName()) + 
				"(", context.getFieldType(), " " + context.getFieldName() + ") {");
		pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();
	}
}