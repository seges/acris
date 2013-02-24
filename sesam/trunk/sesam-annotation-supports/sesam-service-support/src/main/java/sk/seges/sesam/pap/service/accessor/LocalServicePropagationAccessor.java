package sk.seges.sesam.pap.service.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.annotation.LocalServicePropagation;

public class LocalServicePropagationAccessor extends AnnotationAccessor {

	private Class<?>[] interfaces = null;
	
	public LocalServicePropagationAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);

		LocalServicePropagation annotation = getAnnotation(element, LocalServicePropagation.class);
		
		if (annotation != null) {
			interfaces = annotation.interfaces();
		}
	}

	@Override
	public boolean isValid() {
		return interfaces != null;
	}
	
	public Class<?>[] getInterfaces() {
		return interfaces;
	}
}
