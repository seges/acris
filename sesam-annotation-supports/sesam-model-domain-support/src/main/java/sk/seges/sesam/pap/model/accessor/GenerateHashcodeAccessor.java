package sk.seges.sesam.pap.model.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;

public class GenerateHashcodeAccessor extends AnnotationAccessor {

	private Boolean generate = null;
	
	public GenerateHashcodeAccessor(Element element) {
		GenerateHashcode annotation = element.getAnnotation(GenerateHashcode.class);
		
		if (annotation != null) {
			generate = annotation.generate();
		} else {
			generate = true;
		}
	}
	
	@Override
	public boolean isValid() {
		return generate != null;
	}

	public boolean generate() {
		return generate;
	}
}
