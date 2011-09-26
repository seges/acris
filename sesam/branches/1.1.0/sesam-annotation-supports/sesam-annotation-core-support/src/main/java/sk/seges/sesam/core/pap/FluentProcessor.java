package sk.seges.sesam.core.pap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
public abstract class FluentProcessor extends AbstractConfigurableProcessor {
	private List<NamedType> targetClassNames;
	private List<Rule> interfaces = new ArrayList<Rule>();
	private Rule superClass = null;

	protected void addTargetClassName() {}

	// @Override
	// protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
	// return (NamedType[]) targetClassNames.toArray();
	// }

	protected void setSuperClass(Type superClass) {
		this.superClass = new AlwaysRule(superClass);
	}

	protected void setSuperClass(Rule rule) {
		this.superClass = rule;
	}

	protected void addImplementedInterface(Type clz) {
		addImplementedInterface(new AlwaysRule(clz));
	}

	protected void addImplementedInterface(Rule rule) {
		this.interfaces.add(rule);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		ImmutableType immutableType = nameTypesUtils.toImmutableType(typeElement);

		switch (type) {
		case OUTPUT_INTERFACES:
			if (!interfaces.isEmpty()) {
				for (Rule rule : interfaces) {
					if (rule.evaluate(type, typeElement)) {
						Type[] ifaces = new Type[rule.getTypes(immutableType).size()];
						rule.getTypes(immutableType).toArray(ifaces);
						return ifaces;
					}
				}
			}
		case OUTPUT_SUPERCLASS:
			if (superClass != null && superClass.evaluate(type, typeElement)) {
				return new Type[] { superClass.getTypes(immutableType).get(0) };
			}
		}
		return super.getOutputDefinition(type, typeElement);
	}

	public abstract class Rule {
		private final List<Type> types;

		public Rule(Type... types) {
			this.types = Arrays.asList(types);
		}

		public abstract boolean evaluate(OutputDefinition type, TypeElement typeElement);

		public List<Type> getTypes(ImmutableType typeElement) {
			return types;
		}

		protected List<Type> asList(Type... types) {
			return Arrays.asList(types);
		}
	}

	public class AlwaysRule extends Rule {
		public AlwaysRule(Type... types) {
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
		pw.println("this.", fieldName, " = ("+type+")", fieldName, ";");
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

	protected void printHashCode(FormattedPrintWriter pw, NamedType outputName, List<? extends Element> elements) {
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

	protected void printEquals(FormattedPrintWriter pw, NamedType outputName, List<? extends Element> elements) {
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

	protected void doForAllMembers(TypeElement typeElement, ElementKind kind, ElementAction action) {
		List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
		for (Element member : allMembers) {
			if (kind.equals(member.getKind())) {
				action.execute(member);
			}
		}
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
