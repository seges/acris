package sk.seges.sesam.core.pap.model.api;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;

public interface HasAnnotations {

	HasAnnotations annotateWith(AnnotationMirror annotationMirror);
	HasAnnotations annotateWith(MutableAnnotationMirror mutableAnnotationMirror);
	
    <A extends Annotation> A getAnnotation(Class<A> annotationType);

    Set<AnnotationMirror> getAnnotations();
    Set<MutableAnnotationMirror> getMutableAnnotations();
}