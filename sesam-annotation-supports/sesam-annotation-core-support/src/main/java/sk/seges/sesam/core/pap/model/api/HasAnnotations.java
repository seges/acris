package sk.seges.sesam.core.pap.model.api;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

public interface HasAnnotations {

	void annotateWith(AnnotationMirror annotationMirror);
	
    <A extends Annotation> A getAnnotation(Class<A> annotationType);

    Set<AnnotationMirror> getAnnotations();
}