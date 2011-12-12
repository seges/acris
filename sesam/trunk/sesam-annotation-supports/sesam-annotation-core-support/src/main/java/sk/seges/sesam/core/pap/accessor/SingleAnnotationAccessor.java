package sk.seges.sesam.core.pap.accessor;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class SingleAnnotationAccessor<T extends Annotation> extends AnnotationAccessor {

	protected T annotation;
	protected final Element element;
	
	public SingleAnnotationAccessor(Element element, Class<T> clazz, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.element = element;
		annotation = getAnnotation(element, clazz);
	}
	
	@Override
	public boolean isValid() {
		return annotation != null;
	}

}
