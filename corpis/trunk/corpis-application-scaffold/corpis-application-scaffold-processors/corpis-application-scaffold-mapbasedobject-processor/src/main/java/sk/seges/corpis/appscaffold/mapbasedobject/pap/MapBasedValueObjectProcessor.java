/**
 * 
 */
package sk.seges.corpis.appscaffold.mapbasedobject.pap;

import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.appscaffold.mapbasedobject.pap.configurer.MapBasedValueObjectProcessorConfigurer;
import sk.seges.corpis.appscaffold.mapbasedobject.pap.model.MapBasedType;
import sk.seges.corpis.appscaffold.shared.annotation.Hint;
import sk.seges.corpis.appscaffold.shared.annotation.MapBased;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * @author ladislav.gazo
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MapBasedValueObjectProcessor extends MutableAnnotationProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new MapBasedValueObjectProcessorConfigurer();
	}
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new MapBasedType(context.getTypeElement(), processingEnv)
		};
	}

	@Override
	protected void processElement(ProcessorContext context) {
		
		TypeElement element = context.getTypeElement();
		
		MapBased annotation = element.getAnnotation(MapBased.class);
		if (annotation == null) {
			return;
		}

		FormattedPrintWriter pw = context.getPrintWriter();

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