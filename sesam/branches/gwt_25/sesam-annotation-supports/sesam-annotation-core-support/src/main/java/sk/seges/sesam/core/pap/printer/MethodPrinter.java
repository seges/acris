package sk.seges.sesam.core.pap.printer;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MethodPrinter {

	private final FormattedPrintWriter printWriter;
	private final MutableProcessingEnvironment processingEnv;
	
	public MethodPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.printWriter = pw;
		this.processingEnv = processingEnv;
	}
	
	public void printMethodDefinition(ExecutableElement method, TypeElement owner) {
		for (Modifier modifier: method.getModifiers()) {
			if (!modifier.equals(Modifier.ABSTRACT)) {
				printWriter.print(modifier.toString() + " ");
			}
		}

		printType(method.getReturnType(), owner);
		printWriter.print(" " + method.getSimpleName().toString() + "(");
		
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				printWriter.print(", ");
			}
			printType(parameter.asType(), owner);
			printWriter.print(" " + parameter.getSimpleName().toString());
			i++;
		}
		
		printWriter.print(")");
	}

	public void printMethodDefinition(MutableExecutableType method) {
		List<MutableTypeVariable> typeVariables = method.getTypeVariables();
		
		for (Modifier modifier: method.getModifiers()) {
			printWriter.print(modifier.toString() + " ");
		}

		int i = 0;
		
		for (MutableTypeVariable typeVariable: typeVariables) {
			if (i == 0) {
				printWriter.print("<");
			} else {
				printWriter.print(", ");
			}
			printWriter.print(typeVariable);
			i++;
		}

		if (i > 0) {
			printWriter.print("> ");
		}

		if (method.getReturnType() != null) {
			printWriter.print(ProcessorUtils.stripTypeParametersTypes(method.getReturnType()), " ");
		}
		
		printWriter.print(method.getSimpleName().toString() + "(");
		
		i = 0;
		for (MutableVariableElement parameter: method.getParameters()) {
			if (i > 0) {
				printWriter.print(", ");
			}
			printWriter.print(ProcessorUtils.stripTypeParametersTypes(parameter.asType()), " " + parameter.getSimpleName().toString());
			i++;
		}
		
		printWriter.print(")");
	}
	
	private void printType(TypeMirror typeMirror, TypeElement owner) {
		switch (typeMirror.getKind()) {
		case TYPEVAR:
			TypeVariable variable = (TypeVariable) typeMirror;
			if (owner == null) {
				if (variable.getUpperBound() != null) {
					printWriter.print(variable.getUpperBound());
				} else if (variable.getLowerBound() != null) {
					printWriter.print(variable.getLowerBound());
				} else {
					printWriter.print(variable.asElement().getSimpleName());
				}
			} else {
				TypeMirror erasure = ProcessorUtils.erasure(owner, variable);
				
				if (erasure == null) {
					printWriter.print(processingEnv.getTypeUtils().getTypeVariable(((TypeVariable) variable).asElement().getSimpleName().toString()));
				} else {
					printWriter.print(erasure);
				}
			}
			break;
		default:
			printWriter.print(typeMirror);
		}
	}	
}