package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public abstract class AbstractSettingsElementPrinter implements SettingsElementPrinter {

	protected MutableProcessingEnvironment processingEnv;
	
	protected AbstractSettingsElementPrinter(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected TypeMirror unboxType(TypeMirror type) {
		if (type.getKind().isPrimitive()) {
			return processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType();
		}
		
		return type;
	}
}
