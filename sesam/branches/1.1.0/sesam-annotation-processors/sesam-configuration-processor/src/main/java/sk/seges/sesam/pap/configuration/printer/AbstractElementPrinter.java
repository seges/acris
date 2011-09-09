package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public abstract class AbstractElementPrinter implements ElementPrinter {

	protected ProcessingEnvironment processingEnv;
	
	protected AbstractElementPrinter(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected TypeMirror unboxType(TypeMirror type) {
		if (type.getKind().isPrimitive()) {
			return processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType();
		}
		
		return type;
	}
}
