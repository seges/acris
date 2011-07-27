package sk.seges.sesam.core.pap.utils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class MethodHelper {

	private ProcessingEnvironment processingEnv;
	private NameTypesUtils nameTypes;
	
	public MethodHelper(ProcessingEnvironment processingEnv, NameTypesUtils nameTypes) {
		this.processingEnv = processingEnv;
		this.nameTypes = nameTypes;
	}
	
	public void copyMethodDefinition(ExecutableElement method, ClassSerializer serializer, PrintWriter pw) {
		for (Modifier modifier: method.getModifiers()) {
			pw.print(modifier.toString() + " ");
		}
		pw.print(method.getReturnType() + " " + method.getSimpleName().toString() + "(");
		
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(nameTypes.toType(parameter.asType()).toString(null, serializer, true) + " " + parameter.getSimpleName().toString());
			i++;
		}
		
		pw.print(";)");
	}
	
	public void copyAnnotations(Element element, PrintWriter pw) {
		for (AnnotationMirror annotation: element.getAnnotationMirrors()) {
			pw.print("@" + nameTypes.toType(annotation.getAnnotationType()).toString(ClassSerializer.CANONICAL) + "(");
			int i = 0;
			for (Entry<? extends ExecutableElement, ? extends AnnotationValue> annotationValue: 
				processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
				if (i > 0) {
					pw.print(", ");
					pw.println("		");
				}
				pw.print( annotationValue.getKey().getSimpleName() + " = " + annotationValue.getValue().toString());
				i++;
			}
			pw.println(")");
		}
	}

	public void copyConstructors(NamedType outputName, TypeElement fromType, PrintWriter pw) {

		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			pw.print("public " + outputName.getSimpleName() + "(");
			int i = 0;
			for (VariableElement parameter: constructor.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
					pw.print(((DeclaredType)parameter.asType()).asElement().getSimpleName().toString() + " " + parameter.getSimpleName().toString());
				} else {
					pw.print(parameter.asType().toString() + " " + parameter.getSimpleName().toString());
				}
				i++;
			}
			pw.println(") {");
			pw.print("super(");
			i = 0;
			for (VariableElement parameter: constructor.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println("}");
			pw.println();
		}
	}
}