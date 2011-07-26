package sk.seges.sesam.core.pap.model.api;

import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

public interface HasAnnotations {
	void annotateWith(AnnotationMirror annotationMirror);
	
	Set<AnnotationMirror> getAnnotations();
}