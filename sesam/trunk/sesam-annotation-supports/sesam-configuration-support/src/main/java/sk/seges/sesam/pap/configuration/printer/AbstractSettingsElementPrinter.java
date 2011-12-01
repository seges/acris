package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public abstract class AbstractSettingsElementPrinter implements AbstractElementPrinter<SettingsContext> {

	protected MutableProcessingEnvironment processingEnv;
	
	protected AbstractSettingsElementPrinter(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected TypeMirror boxType(TypeMirror type) {
		if (type.getKind().isPrimitive()) {
			return processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType();
		}
		
		return type;
	}
	
	public abstract ElementKind getSupportedType();
}
