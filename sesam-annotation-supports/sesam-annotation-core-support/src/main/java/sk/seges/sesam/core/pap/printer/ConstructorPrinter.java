package sk.seges.sesam.core.pap.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

public class ConstructorPrinter {

	private final MutableDeclaredType ownerType;
	private final MutableProcessingEnvironment processingEnv;
	
	public ConstructorPrinter(MutableDeclaredType ownerType, MutableProcessingEnvironment processingEnv) {
		this.ownerType = ownerType;
		this.processingEnv = processingEnv;
	}

	protected interface ConstructorHelper {

		boolean existsParameter(String field, ExecutableElement constructor, ParameterElement... additionalParameters);
		boolean existsField(String field, ExecutableElement constructor);

		void construct(TypeMirror type, FormattedPrintWriter pw);
		void construct(MutableTypeMirror type, FormattedPrintWriter pw);

		void finish(List<String> initializedFields, ExecutableElement superConstructor, FormattedPrintWriter pw, ParameterElement... additionalParameters);
	}

	protected class DefaultConstructorHelper implements ConstructorHelper {

		@Override
		public boolean existsParameter(String field, ExecutableElement constructor, ParameterElement... additionalParameters) {
			for (VariableElement parameter: constructor.getParameters()) {
				if (ensureParameterName(parameter, additionalParameters).equals(field)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void construct(TypeMirror type, FormattedPrintWriter pw) {
			construct(processingEnv.getTypeUtils().toMutableType(type), pw);
		}

		@Override
		public void construct(MutableTypeMirror type, FormattedPrintWriter pw) {
			pw.print("new ", type, "()");
		}

		@Override
		public void finish(List<String> initializedFields, ExecutableElement superConstructor, FormattedPrintWriter pw, ParameterElement... additionalParameters) {
			for (VariableElement parameter: superConstructor.getParameters()) {
				String parameterName = getParameterName(parameter, additionalParameters);
				
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
	
	private String getParameterName(VariableElement parameter, ParameterElement... additionalParameters) {
		
		MutableTypes typeUtils = processingEnv.getTypeUtils();
				
		for (ParameterElement additionalParameter: additionalParameters) {
			if (additionalParameter.isPropagated() && typeUtils.isSameType(typeUtils.toMutableType(parameter.asType()), additionalParameter.getType())) {
				return additionalParameter.getName();
			}
		}
		
		return null;
	}

	private String ensureParameterName(VariableElement parameter, ParameterElement... additionalParameters) {
		String parameterName = getParameterName(parameter, additionalParameters);
		
		if (parameterName != null) {
			return parameterName;
		}
		
		return parameter.getSimpleName().toString();
	}
	
	protected void printConstructor(ExecutableElement constructor, ExecutableElement superConstructor, ConstructorHelper constructorPrinter, boolean callSuperConstructor, ParameterElement... additionalParameters) {

		List<String> initializedFields = new ArrayList<String>();
		
		HierarchyPrintWriter printWriter = ownerType.getConstructor().getPrintWriter();
		
		if (callSuperConstructor) {
			printWriter.print("super(");
		} else {
			printWriter.print("this(");
		}
		
		int i = 0;
		for (VariableElement parameter: superConstructor.getParameters()) {
			if (i > 0) {
				printWriter.print(", ");
			}
			
			String parameterName = ensureParameterName(parameter, additionalParameters);
			
			if (constructorPrinter.existsParameter(parameterName, constructor, additionalParameters)) {
				printWriter.print(parameterName);
			} else {
				constructorPrinter.construct(parameter.asType(), printWriter);
			}
			initializedFields.add(parameterName);
			i++;
		}
		
		if (callSuperConstructor) {
			printWriter.println(");");
//			constructorPrinter.finish(initializedFields, superConstructor, printWriter, additionalParameters);
		} else {
			for (ParameterElement additionalParameter: additionalParameters) {
				if (i > 0) {
					printWriter.print(", ");
				}
				printWriter.print(additionalParameter.getName().toString());
				i++;
			}
			printWriter.println(");");
		}

		for (VariableElement parameter: constructor.getParameters()) {
			String parameterName = getParameterName(parameter, additionalParameters);
			if (parameterName == null) {
				ProcessorUtils.addField(processingEnv, ownerType, processingEnv.getTypeUtils().toMutableType(parameter.asType()), parameter.getSimpleName().toString());
			}
		}
		
		for (ParameterElement additionalParameter: additionalParameters) {
			if (additionalParameter.isPropagated()) {
				ProcessorUtils.addField(processingEnv, ownerType, additionalParameter.getType(), additionalParameter.getName().toString());
			}
		}

	}
}