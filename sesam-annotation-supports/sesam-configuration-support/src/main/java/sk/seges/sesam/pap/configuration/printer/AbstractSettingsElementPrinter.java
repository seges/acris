package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public abstract class AbstractSettingsElementPrinter implements SettingsElementPrinter {

	protected ProcessingEnvironment processingEnv;
	protected NameTypeUtils nameTypesUtils;
	
	protected AbstractSettingsElementPrinter(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}
	
	protected TypeMirror unboxType(TypeMirror type) {
		if (type.getKind().isPrimitive()) {
			return processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType();
		}
		
		return type;
	}
}
