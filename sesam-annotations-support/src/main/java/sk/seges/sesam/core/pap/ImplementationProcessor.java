package sk.seges.sesam.core.pap;

import java.io.PrintWriter;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public abstract class ImplementationProcessor extends AbstractConfigurableProcessor {

	protected abstract TypeElement getInterfaceElement(Element element, RoundEnvironment roundEnv);
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		TypeElement typeInterfaceElement = getInterfaceElement(element, roundEnv);

		for (TypeMirror typeMirror: typeInterfaceElement.getInterfaces()) {
			Element interfaceElement = processingEnv.getTypeUtils().asElement(typeMirror);
			if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
				processSubProcessor(pw, outputName, element, (TypeElement)interfaceElement);
			}
			pw.println();
		}
	}
}