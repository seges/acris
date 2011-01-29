package sk.seges.corpis.core.pap.transfer;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractConfigurableProcessor {

	public static final String TRANSFER_OBJECT_SUFFIX = "DTO";
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(sk.seges.corpis.platform.annotation.TransferObject.class.getCanonicalName());
		return hashSet;
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
				mutableType.addClassSufix(TRANSFER_OBJECT_SUFFIX)
		};
	} 
	
	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] {
					NamedType.THIS
			};
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	private String toMethodProperty(String field) {
		if (field.length() == 1) {
			return field.toUpperCase();
		}
		return field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	@Override 
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		processingEnv.getMessager().printMessage(Kind.ERROR, "Processing class " + element);

		for (Element fieldElement: ElementFilter.fieldsIn(element.getEnclosedElements())) {
			if (fieldElement.getKind().equals(ElementKind.FIELD)) {
				if (((VariableElement)fieldElement).asType().getKind().equals(TypeKind.DECLARED)) {
					DeclaredType declaredType = (DeclaredType)((VariableElement)fieldElement).asType();
					
					pw.println("public " + declaredType.asElement().getSimpleName() + " get" + toMethodProperty(fieldElement.getSimpleName().toString()) + "() {");
					pw.println("return " + fieldElement.getSimpleName() + ";");
					pw.println("}");
					pw.println();
					pw.println("public void set" + toMethodProperty(fieldElement.getSimpleName().toString()) + "(" + declaredType.asElement().getSimpleName() + " " + fieldElement.getSimpleName() + ") {");
					pw.println("this." + fieldElement.getSimpleName() + " = " + fieldElement.getSimpleName() + ";");
					pw.println("}");
				}
			}
		}
	}
}