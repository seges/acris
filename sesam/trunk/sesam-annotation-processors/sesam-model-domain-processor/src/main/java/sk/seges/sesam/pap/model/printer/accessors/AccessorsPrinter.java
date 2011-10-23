package sk.seges.sesam.pap.model.printer.accessors;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.AnnotationPrinter;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.printer.copy.CopyPrinter;

public class AccessorsPrinter extends CopyPrinter {

	private final AnnotationPrinter annotationPrinter;
	
	public AccessorsPrinter(MutableProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(processingEnv, pw);
		
		this.annotationPrinter = new AnnotationPrinter(pw, processingEnv);
	}
	
	@Override
	public void print(TransferObjectContext context) {

		String modifier = Modifier.PUBLIC.toString() + " ";
		
		//modifier = context.getModifier() != null ? (context.getModifier().toString() + " ") : "";
		
		List<AnnotationMirror> supportedAnnotations = getSupportedAnnotations(context);
		
		for (AnnotationMirror supportedAnnotation: supportedAnnotations) {
			annotationPrinter.print(supportedAnnotation);
		}
		
		pw.println(modifier, context.getDtoFieldType(), " " + MethodHelper.toGetter(context.getDtoFieldName()) + " {");
		pw.println("return " + context.getDtoFieldName() + ";");
		pw.println("}");
		pw.println();

		pw.println(modifier + "void " + MethodHelper.toSetter(context.getDtoFieldName()) + 
				"(", context.getDtoFieldType(), " " + context.getDtoFieldName() + ") {");
		pw.println("this." + context.getDtoFieldName() + " = " + context.getDtoFieldName() + ";");
		pw.println("}");
		pw.println();
	}
}