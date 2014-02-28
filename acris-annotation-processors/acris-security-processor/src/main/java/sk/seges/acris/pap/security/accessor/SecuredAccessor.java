package sk.seges.acris.pap.security.accessor;

import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public class SecuredAccessor extends AnnotationAccessor {

	private final Annotation securedAnnotation;

	public SecuredAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);

		securedAnnotation = getAnnotation(element, getSecuredAnnotationClass());
	}

	protected Class<? extends Annotation> getSecuredAnnotationClass() {
		return Secured.class;
	}

	@Override
	public boolean isValid() {
		return securedAnnotation != null;
	}
}
