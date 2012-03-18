package sk.seges.sesam.core.pap.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class ConstructorPrinter {

	private final FormattedPrintWriter pw;
	private final MutableDeclaredType outputName;
	
	public ConstructorPrinter(FormattedPrintWriter pw, MutableDeclaredType outputName) {
		this.pw = pw;
		this.outputName = outputName;
	}

	protected interface ConstructorHelper {

		boolean existsParameter(String field, ExecutableElement constructor);
		boolean existsField(String field, ExecutableElement constructor);

		void construct(TypeMirror type, FormattedPrintWriter pw);
		void construct(MutableTypeMirror type, FormattedPrintWriter pw);

		void finish(List<String> initializedFields, ExecutableElement superConstructor, FormattedPrintWriter pw);
	}

	protected class DefaultConstructorHelper implements ConstructorHelper {

		@Override
		public boolean existsParameter(String field, ExecutableElement constructor) {
			for (VariableElement parameter: constructor.getParameters()) {
				if (parameter.getSimpleName().toString().equals(field)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void construct(TypeMirror type, FormattedPrintWriter pw) {
			pw.print("new " + type.toString() + "()");
		}

		@Override
		public void construct(MutableTypeMirror type, FormattedPrintWriter pw) {
			pw.print("new ", type, "()");
		}

		@Override
		public void finish(List<String> initializedFields, ExecutableElement superConstructor, FormattedPrintWriter pw) {
			for (VariableElement parameter: superConstructor.getParameters()) {
				String parameterName = parameter.getSimpleName().toString();
				
				if (!initializedFields.contains(parameterName)) {
					pw.print("this." + parameterName + " = ");
					construct(parameter.asType(), pw);
					pw.println(";");
				}
			}

			for (VariableElement parameter: superConstructor.getParameters()) {
				String parameterName = parameter.getSimpleName().toString();
				if (!initializedFields.contains(parameterName)) {
					pw.print("this." + parameterName + " = ");
					construct(parameter.asType(), pw);
					pw.println(";");
				}
			}
		}

		@Override
		public boolean existsField(String field, ExecutableElement constructor) {
			List<VariableElement> fields = ElementFilter.fieldsIn(constructor.getEnclosingElement().getEnclosedElements());
			
			for (VariableElement classField: fields) {
				if (classField.getSimpleName().equals(field)) {
					return true;
				}
			}
			
			return false;
		}
	}

	public void printConstructors(TypeElement fromType, final ExecutableElement superTypeConstructorElement, boolean callSuperConstructor, ParameterElement... additionalParameters) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			printConstructor(constructor, superTypeConstructorElement, new DefaultConstructorHelper(), callSuperConstructor, additionalParameters);
		}
	}
	
	public void printConstructors(TypeElement fromType, ParameterElement... additionalParameters) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			printConstructor(constructor, additionalParameters);
		}
	}
	
	protected void printConstructor(ExecutableElement constructor, ParameterElement... additionalParameters) {
		printConstructor(constructor, constructor, new DefaultConstructorHelper(), true, additionalParameters);
	}
		
	protected void printConstructor(ExecutableElement constructor, ExecutableElement superConstructor, ConstructorHelper constructorPrinter, boolean callSuperConstructor, ParameterElement... additionalParameters) {

		List<String> initializedFields = new ArrayList<String>();
		
		pw.print("public " + outputName.getSimpleName() + "(");
		int i = 0;
		for (VariableElement parameter: constructor.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = parameter.getSimpleName().toString();
			
			if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
				pw.print(((DeclaredType)parameter.asType()).asElement().getSimpleName().toString() + " " + parameterName);
			} else {
				pw.print(parameter.asType().toString() + " " + parameterName);
			}
			
			i++;
		}
		
		for (ParameterElement additionalParameter: additionalParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = additionalParameter.getName().toString();
			
//			if (additionalParameter.asType().getKind().equals(TypeKind.DECLARED)) {
//				String simpleTypeName =  .asElement().getSimpleName().toString();
//				pw.print(simpleTypeName + " " + parameterName);
//			} else {
			
				pw.print(additionalParameter.getType().toString(ClassSerializer.SIMPLE, true) + " " + parameterName);
//			}

			i++;
		}
		
		pw.println(") {");
		if (callSuperConstructor) {
			pw.print("super(");
		} else {
			pw.print("this(");
		}
		i = 0;
		for (VariableElement parameter: superConstructor.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = parameter.getSimpleName().toString();
			if (constructorPrinter.existsParameter(parameterName, constructor)) {
				pw.print(parameterName);
			} else {
				constructorPrinter.construct(parameter.asType(), pw);
			}
			initializedFields.add(parameterName);
			i++;
		}
		
		if (callSuperConstructor) {
			pw.println(");");
			for (VariableElement parameter: superConstructor.getParameters()) {
				String name = parameter.getSimpleName().toString();
				if (!constructorPrinter.existsField(name, superConstructor)) {
					pw.println("this." + name + " = " + name + ";");
					initializedFields.add(name);
				}
			}
			
			for (ParameterElement additionalParameter: additionalParameters) {
				String name = additionalParameter.getName();
				if (!constructorPrinter.existsField(name, superConstructor)) {
					pw.println("this." + name + " = " + name + ";");
					initializedFields.add(name);
				}
			}

			constructorPrinter.finish(initializedFields, superConstructor, pw);
		} else {
			for (ParameterElement additionalParameter: additionalParameters) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(additionalParameter.getName().toString());
				i++;
			}
			pw.println(");");
		}
		
		pw.println("}");
		pw.println();
	}
}