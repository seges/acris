package sk.seges.sesam.pap.model.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;

public class GenerateHashcodeAccessor extends SingleAnnotationAccessor<GenerateHashcode> {

	public GenerateHashcodeAccessor(Element element) {
		super(element, GenerateHashcode.class);
	}
	
	public boolean generate() {
		if (annotation != null) {
			return annotation.generate();
		}
		return true;
	}
}