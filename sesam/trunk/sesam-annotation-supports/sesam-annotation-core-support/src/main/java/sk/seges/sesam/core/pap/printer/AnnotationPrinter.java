package sk.seges.sesam.core.pap.printer;

import java.util.List;
import java.util.Map.Entry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor.AnnotationFilter;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class AnnotationPrinter {
	
	private final FormattedPrintWriter pw;
	private final MutableProcessingEnvironment processingEnv;
	
	public AnnotationPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}

	public void printMethodAnnotations(Element method, AnnotationFilter...annotationFilters) {
		for (AnnotationMirror annotation: method.getAnnotationMirrors()) {
			
			boolean isAnnotationIgnored = false;
			
			if (annotationFilters != null) {
				for (AnnotationFilter filter: annotationFilters) {
					if (filter.isAnnotationIgnored(annotation)) {
						isAnnotationIgnored = true;
						break;
					}
				}
			}
			
			if (!isAnnotationIgnored) {
				print(annotation);
			}
		}
	}

	public void print(AnnotationMirror annotation) {
		pw.print("@", processingEnv.getTypeUtils().toMutableType(annotation.getAnnotationType()));
		
		if (annotation.getElementValues().size() > 0) {
			pw.print("(");
			int i = 0;
			for (Entry<? extends ExecutableElement, ? extends AnnotationValue> annotationValue: annotation.getElementValues().entrySet()) {
				if (i > 0) {
					pw.print(", ");
					
					if (pw.getCurrentPosition() > FormattedPrintWriter.LINE_LENGTH) {
						pw.println();
						pw.print("		");
					}
				}
				pw.print( annotationValue.getKey().getSimpleName() + " = ");
				printValue(annotationValue.getValue());
				i++;
			}
			pw.print(")");
		}
		pw.println();
	}
	
	private void printValue(Object value) {
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
			Object annotationValue = ((AnnotationValue)value).getValue();
			printValue(annotationValue);
			if (annotationValue instanceof TypeMirror) {
				TypeMirror annotationType = (TypeMirror)annotationValue;
				
				if (annotationType.getKind().equals(TypeKind.DECLARED)) {
					pw.print(".class");
				}
			}
		} else if (value instanceof VariableElement) {
			//VariableElement - representing an enum constant
			pw.print(((VariableElement)value).asType(), "." + ((VariableElement)value).toString());
		} else if (value instanceof TypeMirror) {
			//TypeMirror
			pw.print(value);
		} else if (value instanceof List) {
			//List<? extends AnnotationValue> - representing the elements, in declared order, if the value is an array
			if (((List<?>)value).size() > 1) {
				pw.print("{");
			}
			int i = 0;
			for (Object obj: (List<?>)value) {
				if (i > 0) {
					pw.print(", ");
				}
				printValue(obj);
				i++;
			}
			if (((List<?>)value).size() > 1) {
				pw.print("}");
			}
		} else if (value instanceof String) {
			//String
			pw.print("\"" + value.toString().replace("\\", "\\\\") + "\"");
		} else {
			//Integer - primitive types
			pw.print(value.toString());
		}
	}	
}