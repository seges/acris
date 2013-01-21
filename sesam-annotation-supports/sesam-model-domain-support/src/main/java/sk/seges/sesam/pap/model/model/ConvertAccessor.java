package sk.seges.sesam.pap.model.model;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.annotation.Convert;

public class ConvertAccessor extends AnnotationAccessor {

	private final Convert convertAnnotation;

	public ConvertAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.convertAnnotation = this.getAnnotation(element, Convert.class);
	}
	
	@Override
	public boolean isValid() {
		return convertAnnotation != null;
	}

	public boolean getValue() {
		return convertAnnotation.value();
	}
}