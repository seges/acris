package sk.seges.sesam.core.pap.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MethodPrinter {

	private final FormattedPrintWriter pw;
	private final NameTypeUtils nameTypeUtils;
	
	public MethodPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.nameTypeUtils = new NameTypeUtils(processingEnv);
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
			pw.print(nameTypeUtils.toType(parameter.asType()), " " + parameter.getSimpleName().toString());
			i++;
		}
		
		pw.print(")");
	}	

}
