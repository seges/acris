package sk.seges.sesam.core.pap.accessor;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

public abstract class SingleAnnotationAccessor<T extends Annotation> extends AnnotationAccessor {

	protected T annotation;
	
	public SingleAnnotationAccessor(Element element, Class<T> clazz) {
		annotation = getAnnotation(element, clazz);
	}
	
	@Override
	public boolean isValid() {
		return annotation != null;
	}

}
