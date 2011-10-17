package sk.seges.sesam.core.pap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
public abstract class FluentProcessor extends MutableAnnotationProcessor {

	public class FluentOutput extends DelegateMutableDeclaredType {

		private final MutableDeclaredType mutableFluentInput;

		public FluentOutput(TypeElement fluentInput, MutableProcessingEnvironment processingEnv) {
			this.mutableFluentInput = processingEnv.getTypeUtils().toMutableType((DeclaredType) fluentInput.asType());

			setKind(resultKind);

			if (!interfaces.isEmpty()) {
				Set<MutableDeclaredType> implementedInterfaces = new HashSet<MutableDeclaredType>();

				for (Rule rule : interfaces) {
					if (rule.evaluate(OutputDefinition.OUTPUT_INTERFACES, fluentInput)) {
						implementedInterfaces.addAll(rule.getTypes(mutableFluentInput.clone()));
					}
				}

				setInterfaces(implementedInterfaces);
			}

			if (superClass != null) {
				if (superClass.evaluate(OutputDefinition.OUTPUT_SUPERCLASS, fluentInput)) {
					setSuperClass(superClass.getTypes(mutableFluentInput.clone()).get(0));
				}
			}
		}

		@Override
		protected MutableDeclaredType getDelegate() {
			return FluentProcessor.this.getResultType(mutableFluentInput.clone());
		}

	}

	private MutableTypeKind resultKind = MutableTypeKind.CLASS;
	private List<Rule> interfaces = new ArrayList<Rule>();
	private Rule superClass = null;
	private Type[] reactsOn;
	private DefaultProcessorConfigurer configurer = null;
	protected FormattedPrintWriter pw;

	@Override
	protected void processElement(ProcessorContext context) {
		pw = context.getPrintWriter();
		
		doProcessElement(context);
	}
	
	protected void addTargetClassName() {}

	protected abstract void doProcessElement(ProcessorContext context);
	protected abstract MutableDeclaredType getResultType(MutableDeclaredType inputType);

	public void setResultKind(MutableTypeKind resultKind) {
		this.resultKind = resultKind;
	}

	protected void setSuperClass(Class<?> superClass) {
		MutableTypes types = new MutableTypes(null, null, null);
		setSuperClass(types.toMutableType(superClass));
	}
	
	protected void setSuperClass(MutableDeclaredType superClass) {
		this.superClass = new AlwaysRule(superClass);
	}

	protected void setSuperClass(Rule rule) {
		this.superClass = rule;
	}

	protected void addImplementedInterface(Class<?> iface) {
		MutableTypes types = new MutableTypes(null, null, null);
		addImplementedInterface(types.toMutableType(iface));
	}
	
	protected void addImplementedInterface(MutableDeclaredType clz) {
		addImplementedInterface(new AlwaysRule(clz));
	}

	protected void addImplementedInterface(Rule rule) {
		this.interfaces.add(rule);
	}

	protected void reactsOn(Type... annotations) {
		reactsOn = annotations;
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { new FluentOutput(context.getTypeElement(), processingEnv) };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		if (configurer == null) {
			configurer = new DefaultProcessorConfigurer() {
				@Override
				protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
					switch (element) {
					case PROCESSING_ANNOTATIONS:
						return reactsOn;
					}
					return new Type[] {};
				}
			};
		}
		return configurer;
	}
	
	protected MutableDeclaredType toParametrizedMutableDeclaredType(Class<?> baseType, Class<?>... typeVariables) {
		MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType(baseType);
		for(Class<?> typeVariable : typeVariables) {
			MutableDeclaredType typeVariableMirror = processingEnv.getTypeUtils().toMutableType(typeVariable);
			mutableType.addTypeVariable(processingEnv.getTypeUtils().getTypeVariable(null, typeVariableMirror));
		}
		return mutableType;
	}
	
	protected MutableDeclaredType toParametrizedMutableDeclaredType(Class<?> baseType, MutableDeclaredType... typeVariables) {
		MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType(baseType);
		return toParametrizedMutableDeclaredType(mutableType, typeVariables);
	}
	
	protected MutableDeclaredType toParametrizedMutableDeclaredType(MutableDeclaredType baseType, MutableDeclaredType... typeVariables) {
		for(MutableDeclaredType typeVariable : typeVariables) {
			baseType.addTypeVariable(processingEnv.getTypeUtils().getTypeVariable(null, typeVariable));
		}
		return baseType;
	}
	
	/**
	 * Example: rule defines an interface that generated class must implement.
	 * The interface definition is defined using
	 * {@link #getTypes(MutableDeclaredType)} method. The rule will be applied (
	 * in this case the class will implement the interface) when
	 * {@link #evaluate(OutputDefinition, TypeElement)} method returns true.
	 * 
	 * In case you want to provide customized type based on some logic, you
	 * don't provide resulting type(s) in constructor. Then you need to override
	 * {@link #getTypes(MutableDeclaredType)} method.
	 * 
	 * @see AlwaysRule
	 * @see #asList(Object...)
	 */
	public abstract class Rule {
		private final List<MutableDeclaredType> types;

		public Rule(MutableDeclaredType... types) {
			this.types = Arrays.asList(types);
		}

		/**
		 * @param type
		 * @param typeElement
		 * @return True when the rule should be applied
		 */
		public abstract boolean evaluate(OutputDefinition type, TypeElement typeElement);

		public List<MutableDeclaredType> getTypes(MutableDeclaredType typeElement) {
			return types;
		}

		/**
		 * Helper method so you can easily return generated types.
		 * 
		 * @param <T>
		 * @param types
		 * @return
		 */
		protected <T> List<T> asList(T... types) {
			return Arrays.asList(types);
		}
	}

	/**
	 * A rule that applies always.
	 */
	public class AlwaysRule extends Rule {
		public AlwaysRule(MutableDeclaredType... types) {
			super(types);
		}

		@Override
		public boolean evaluate(OutputDefinition type, TypeElement typeElement) {
			return true;
		}
	}

	protected void printField(FormattedPrintWriter pw, Object type, Element name) {
		pw.println("private ", type, " ", name.getSimpleName(), ";");
	}

	protected String getGetterSignature(Object type, Element name) {
		String fieldName = name.getSimpleName().toString();
		String upperFieldName = firstUpperCase(fieldName);

		return type.toString() + " get" + upperFieldName + "()";
	}

	protected String getSetterSignature(Object type, Element name) {
		String fieldName = name.getSimpleName().toString();
		String upperFieldName = firstUpperCase(fieldName);

		return "void set" + upperFieldName + "(" + type.toString() + " " + fieldName + ")";
	}

	protected void printStandardGetter(FormattedPrintWriter pw, Object type, Element name) {
		String fieldName = name.getSimpleName().toString();
		String upperFieldName = firstUpperCase(fieldName);

		pw.println("public ", type, " get", upperFieldName, "() {");
		pw.println("return ", fieldName, ";");
		pw.println("}");
	}

	protected void printStandardSetter(FormattedPrintWriter pw, Object type, ExecutableElement name) {
		String fieldName = name.getSimpleName().toString();
		String upperFieldName = firstUpperCase(fieldName);

		pw.println("public void set", upperFieldName, "(", type, " ", fieldName, ") {");
		pw.println("this.", fieldName, " = ", fieldName, ";");
		pw.println("}");

	}

	protected void printObjectSetter(FormattedPrintWriter pw, Object type, ExecutableElement name) {
		String fieldName = name.getSimpleName().toString();
		String upperFieldName = firstUpperCase(fieldName);

		pw.println("public void set", upperFieldName, "(Object ", fieldName, ") {");
		pw.println("this.", fieldName, " = (" + type + ")", fieldName, ";");
		pw.println("}");
	}

	protected String firstUpperCase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	protected boolean typeEquals(Class<?> a, AnnotationMirror b) {
		return typeEquals(a, b.getAnnotationType());
	}

	protected boolean typeEquals(Class<?> a, TypeMirror b) {
		TypeMirror mirrorA = processingEnv.getElementUtils().getTypeElement(a.getCanonicalName()).asType();
		return processingEnv.getTypeUtils().isSameType(mirrorA, b);
	}

	protected void printHashCode(FormattedPrintWriter pw, MutableDeclaredType outputName,
			List<? extends Element> elements) {
		pw.println("@", Override.class);
		pw.println("public int hashCode() {");
		pw.println("final int prime = 31;");
		pw.println("int result = 1;");
		for (Element element : elements) {
			pw.println("result = prime * result + ((", element.getSimpleName(), " == null) ? 0 : ",
					element.getSimpleName(), ".hashCode());");
		}
		pw.println("return result;");
		pw.println("}");
	}

	protected void printEquals(FormattedPrintWriter pw, MutableDeclaredType outputName, List<? extends Element> elements) {
		pw.println("@", Override.class);
		pw.println("public boolean equals(Object obj) {");
		pw.println("if (this == obj)");
		pw.println("return true;");
		pw.println("if (obj == null)");
		pw.println("return false;");
		pw.println("if (getClass() != obj.getClass())");
		pw.println("return false;");
		pw.println(outputName, " other = (", outputName, ") obj;");
		for (Element element : elements) {
			Name simpleName = element.getSimpleName();
			pw.println("if (", simpleName, " == null) {");
			pw.println("if (other.", simpleName, " != null)");
			pw.println("return false;");
			pw.println("} else if (!", simpleName, ".equals(other.", simpleName, ")) {");
			pw.println("return false;");
			pw.println("}");
		}
		pw.println("return true;");
		pw.println("}");
	}

	@SuppressWarnings("unchecked")
	protected void doForAllMembers(TypeElement typeElement, ElementKind kind,
			@SuppressWarnings("rawtypes") ElementAction action) {
		List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
		for (Element member : allMembers) {
			if (kind.equals(member.getKind())) {
				action.execute(member);
			}
		}
	}
	
	protected TypeElement toElement(TypeMirror mirror) {
		if (mirror.getKind().equals(TypeKind.DECLARED)) {
		     return (TypeElement) ((DeclaredType)mirror).asElement();
		}
		return null;
	}
	

	public interface ElementAction<T extends Element> {
		void execute(T element);
	}

	/**
	 * Executes action on a method if the method is not one of Object methods
	 * (it means it filters Object methods). Usually this is the case when you
	 * use the action on the list of methods where all inherited methods are
	 * included.
	 * 
	 * @author ladislav.gazo
	 */
	public abstract class MethodAction implements ElementAction<ExecutableElement> {
		private final TypeElement objectType = processingEnv.getElementUtils().getTypeElement(
				Object.class.getCanonicalName());

		@Override
		public final void execute(ExecutableElement element) {
			List<ExecutableElement> objectMethods = ElementFilter.methodsIn(objectType.getEnclosedElements());
			if (objectMethods.contains(element)) {
				return;
			}

			doExecute(element);
		}

		protected abstract void doExecute(ExecutableElement element);
	}
}
