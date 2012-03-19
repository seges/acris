package sk.seges.sesam.pap.configuration.printer;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.parameter.ModelType;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class FieldWithDefaultsPrinter extends FieldPrinter {

	public FieldWithDefaultsPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(pw, processingEnv);
	}

	@Override
	public void print(SettingsContext context) {
		ExecutableElement method = context.getMethod();

		if (context.getNestedElement() != null) {
			pw.print("private ", context.getNestedMutableType(), " " + context.getFieldName() + " = ");
			pw.println("new ", context.getNestedMutableType(), "();");
			pw.println();
		} else {
			pw.print("private ", boxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString() + " = ");
			printValue(method.getDefaultValue());
			pw.println(";");
			pw.println();
		}
	}

	public void print(AnnotationMirror annotation) {
		pw.print("new ", new ModelType(annotation, processingEnv), "()");
	}
	
	private void printValue(Object value) {
		if (value == null) {
			pw.print("null");
		}
		 /* Represents a value of an annotation type element.
		 * A value is of one of the following types:
		 * <ul><li> a wrapper class (such as {@link Integer}) for a primitive type
		 *     <li> {@code String}
		 *     <li> {@code TypeMirror}
		 *     <li> {@code VariableElement} (representing an enum constant)
		 *     <li> {@code AnnotationMirror}
		 *     <li> {@code List<? extends AnnotationValue>}
		 *		(representing the elements, in declared order, if the value is an array)
		 * </ul>*/
		
		if (value instanceof AnnotationMirror) {
			print((AnnotationMirror)value);
		} else if (value instanceof AnnotationValue) {
			printValue(((AnnotationValue)value).getValue());
		} else if (value instanceof VariableElement) {
			//VariableElement - representing an enum constant
			pw.print(((VariableElement)value).asType(), "." + ((VariableElement)value).toString());
		} else if (value instanceof TypeMirror) {
			//TypeMirror
			pw.print(value);
		} else if (value instanceof List) {
			//List<? extends AnnotationValue> - representing the elements, in declared order, if the value is an array
			pw.print("{");
			int i = 0;
			for (Object obj: (List<?>)value) {
				if (i > 0) {
					pw.print(", ");
				}
				printValue(obj);
				i++;
			}
			pw.print("}");
		} else if (value instanceof String) {
			//String
			pw.print("\"" + value.toString().replace("\\", "\\\\") + "\"");
		} else {
			//Integer - primitive types
			pw.print(value.toString());
		}
	}
}