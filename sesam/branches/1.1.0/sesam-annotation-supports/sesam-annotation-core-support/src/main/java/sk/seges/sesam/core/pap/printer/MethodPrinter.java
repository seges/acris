package sk.seges.sesam.core.pap.printer;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MethodPrinter {

	private final FormattedPrintWriter pw;
	private final MutableProcessingEnvironment processingEnv;
	
	public MethodPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}

	public void printMethodDefinition(ExecutableElement method) {
		for (Modifier modifier: method.getModifiers()) {
			if (!modifier.equals(Modifier.ABSTRACT)) {
				pw.print(modifier.toString() + " ");
			}
		}
		pw.print(method.getReturnType(), " " + method.getSimpleName().toString() + "(");
		
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(processingEnv.getTypeUtils().toMutableType(parameter.asType()), " " + parameter.getSimpleName().toString());
			i++;
		}
		
		pw.print(")");
	}
}