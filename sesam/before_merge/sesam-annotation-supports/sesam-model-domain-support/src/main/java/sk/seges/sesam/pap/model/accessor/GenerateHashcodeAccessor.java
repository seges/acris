package sk.seges.sesam.pap.model.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;

public class GenerateHashcodeAccessor extends SingleAnnotationAccessor<GenerateHashcode> {

	public GenerateHashcodeAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, GenerateHashcode.class, processingEnv);
	}
	
	public boolean generate() {
		if (annotation != null) {
			return annotation.generate();
		}
		return true;
	}
}