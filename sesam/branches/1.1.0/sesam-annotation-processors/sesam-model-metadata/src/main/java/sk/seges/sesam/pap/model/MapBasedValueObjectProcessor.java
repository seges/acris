/**
 * 
 */
package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.model.metadata.annotation.applicationmodel.Hint;
import sk.seges.sesam.model.metadata.annotation.applicationmodel.MapBased;
import sk.seges.sesam.model.metadata.annotation.applicationmodel.MapBasedObject;

/**
 * @author ladislav.gazo
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MapBasedValueObjectProcessor extends AbstractConfigurableProcessor {

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		mutableType.changePackage(mutableType.getPackageName() + ".shared.domain");
		return new NamedType[] { mutableType.addClassSufix("MapBean") };
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { MapBased.class };
		case OUTPUT_SUPERCLASS:
			return new Type[] { MapBasedObject.class };
		case OUTPUT_INTERFACES:
			List<Type> result = new ArrayList<Type>();
			for (TypeMirror interfaceElementMirror : typeElement.getInterfaces()) {
				Element asElement = processingEnv.getTypeUtils().asElement(interfaceElementMirror);
				result.add(new InputClass(processingEnv.getElementUtils().getPackageOf(asElement).toString(),
						asElement.getSimpleName().toString()));
			}
			return result.toArray(new Type[] {});

		}
		return super.getConfigurationTypes(type, typeElement);
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv,
			PrintWriter pw) {
		MapBased annotation = element.getAnnotation(MapBased.class);
		if (annotation == null) {
			return;
		}

		List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(element);
		for (Element member : allMembers) {
			if (!isMethod(member)) {
				continue;
			}

			if (!isDeclaredMember(element, member)) {
				continue;
			}
			ExecutableElement method = (ExecutableElement) member;
			TypeMirror returnType = method.getReturnType();
			if (returnType == null || returnType instanceof NoType || returnType instanceof NullType) {
				continue;
			}

			Name methodName = method.getSimpleName();

			String returnTypeName = returnType.toString();
			if (!returnTypeName.contains(".")) {
				// if it is primitive type, it doesn't have a package
				continue;
			}
			String methodNameStr = methodName.toString();

			Hint hint = method.getAnnotation(Hint.class);

			String key = (hint == null ? methodNameStr : hint.value());

			pw.println("public " + returnTypeName + " " + toGetter(methodNameStr) + "() {");
			pw.println("    return (" + returnTypeName + ") get(\"" + key + "\");");
			pw.println("}");

			pw.println("public void " + toSetter(methodNameStr) + "(" + returnTypeName + " value) {");
			pw.println("    set(\"" + key + "\", value);");
			pw.println("}");
		}
	}

	private boolean isMethod(Element member) {
		return ElementKind.METHOD.equals(member.getKind());
	}

	/**
	 * 
	 * @param element
	 * @param member
	 * @return True if type element is declared member - is not inherited or
	 *         overridden.
	 */
	private boolean isDeclaredMember(TypeElement element, Element member) {
		return processingEnv.getTypeUtils().isSameType(element.asType(),
				member.getEnclosingElement().asType());
	}

	private String toGetter(String field) {
		return "get" + toMethodProperty(field);
	}

	private String toSetter(String field) {
		return "set" + toMethodProperty(field);
	}

	private String toMethodProperty(String field) {
		if (field.length() == 1) {
			return field.toUpperCase();
		}
		return field.substring(0, 1).toUpperCase() + field.substring(1);
	}

}