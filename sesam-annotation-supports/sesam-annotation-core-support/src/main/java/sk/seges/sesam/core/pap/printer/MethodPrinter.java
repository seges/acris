package sk.seges.sesam.core.pap.printer;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MethodPrinter {

	private final FormattedPrintWriter pw;
	private final MutableProcessingEnvironment processingEnv;
	
	public MethodPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}

	public void printMethodDefinition(ExecutableElement method, TypeElement owner) {
		for (Modifier modifier: method.getModifiers()) {
			if (!modifier.equals(Modifier.ABSTRACT)) {
				pw.print(modifier.toString() + " ");
			}
		}
		
		printType(method.getReturnType(), owner);
		pw.print(" " + method.getSimpleName().toString() + "(");
		
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			printType(parameter.asType(), owner);
			pw.print(" " + parameter.getSimpleName().toString());
			i++;
		}
		
		pw.print(")");
	}
	
	private void printType(TypeMirror typeMirror, TypeElement owner) {
		switch (typeMirror.getKind()) {
		case TYPEVAR:
			TypeVariable variable = (TypeVariable) typeMirror;
			if (owner == null) {
				if (variable.getUpperBound() != null) {
					pw.print(variable.getUpperBound());
				} else if (variable.getLowerBound() != null) {
					pw.print(variable.getLowerBound());
				} else {
					pw.print(variable.asElement().getSimpleName());
				}
			} else {
				TypeMirror erasure = ProcessorUtils.erasure(owner, variable);
				
				if (erasure == null) {
					pw.print(processingEnv.getTypeUtils().getTypeVariable(((TypeVariable) variable).asElement().getSimpleName().toString()));
				} else {
					pw.print(erasure);
				}
			}
			break;
		default:
			pw.print(typeMirror);
		}
	}	
}