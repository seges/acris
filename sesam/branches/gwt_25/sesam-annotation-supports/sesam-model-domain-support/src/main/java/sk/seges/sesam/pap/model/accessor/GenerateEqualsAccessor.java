package sk.seges.sesam.pap.model.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;

public class GenerateEqualsAccessor extends AnnotationAccessor {

	private Boolean generate = null;
	
	public GenerateEqualsAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		
		GenerateEquals annotation = getAnnotation(element, GenerateEquals.class);
		
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
