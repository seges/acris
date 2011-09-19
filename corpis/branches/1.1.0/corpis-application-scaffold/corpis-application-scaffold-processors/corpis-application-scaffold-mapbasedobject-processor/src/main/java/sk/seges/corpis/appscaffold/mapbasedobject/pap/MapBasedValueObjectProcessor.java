/**
 * 
 */
package sk.seges.corpis.appscaffold.mapbasedobject.pap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.appscaffold.shared.annotation.Hint;
import sk.seges.corpis.appscaffold.shared.annotation.MapBased;
import sk.seges.corpis.appscaffold.shared.domain.MapBasedObject;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MapBasedValueObjectProcessor extends AbstractConfigurableProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new MapBasedValueObjectProcessorConfigurer();
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		//TODO use packageValidator
		return new NamedType[] { mutableType.changePackage(mutableType.getPackageName() + ".shared.domain").addClassSufix("MapBean") };
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] { MapBasedObject.class };
		case OUTPUT_INTERFACES:
			List<Type> result = new ArrayList<Type>();
			for (TypeMirror interfaceElementMirror : typeElement.getInterfaces()) {
				result.add(nameTypesUtils.toImmutableType(interfaceElementMirror));
			}
			return result.toArray(new Type[] {});

		}
		return super.getOutputDefinition(type, typeElement);
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		MapBased annotation = element.getAnnotation(MapBased.class);
		if (annotation == null) {
			return;
		}

		//TODO use ElementFilter -> ElementFilter.methodsIn(element.getEnclosedElements())
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

			//TODO do not use instanceof, use returnType.getKind() instead
			//Using instanceof is not necessarily a reliable idiom for determining the effective class of an object in this modeling
			//hierarchy since an implementation may choose to have a single object implement multiple TypeMirror subinterfaces.
			if (returnType == null || returnType instanceof NoType || returnType instanceof NullType) {
				continue;
			}

			//TODO use returnType.getKind().isPrimitive()
			String returnTypeName = returnType.toString();
			if (!returnTypeName.contains(".")) {
				// if it is primitive type, it doesn't have a package
				continue;
			}
			String methodNameStr = method.getSimpleName().toString();

			Hint hint = method.getAnnotation(Hint.class);

			String key = (hint == null ? methodNameStr : hint.value());

			pw.println("public ", returnType, " " + toGetter(methodNameStr) + "() {");
			pw.println("return (" + returnTypeName + ") get(\"" + key + "\");");
			pw.println("}");

			pw.println("public void " + toSetter(methodNameStr) + "(", returnType, " value) {");
			pw.println("set(\"" + key + "\", value);");
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

	//TODO use methodHelper
	private String toGetter(String field) {
		return "get" + toMethodProperty(field);
	}

	//TODO use methodHelper
	private String toSetter(String field) {
		return "set" + toMethodProperty(field);
	}

	//TODO use methodHelper
	private String toMethodProperty(String field) {
		if (field.length() == 1) {
			return field.toUpperCase();
		}
		return field.substring(0, 1).toUpperCase() + field.substring(1);
	}

}