package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.api.HasAnnotations;

class AnnotationHolderDelegate implements HasAnnotations {

	private Set<AnnotationMirror> annotations;
	private final Element element;
	private final MutableProcessingEnvironment processingEnv;
	
	AnnotationHolderDelegate(MutableProcessingEnvironment processingEnv) {
		this.element = null;
		this.processingEnv = processingEnv;
		this.annotations = new HashSet<AnnotationMirror>();
	}
	
	AnnotationHolderDelegate(MutableProcessingEnvironment processingEnv, Element element) {
		this.element = element;
		this.processingEnv = processingEnv;
	}
	
	public void annotateWith(AnnotationMirror annotation) {
		initializeAnnotations();
		annotations.add(annotation);
	}

	private void copyAnnotations() {
		if (element != null) {
			for (AnnotationMirror annotation: element.getAnnotationMirrors()) {
				annotations.add(annotation);
			}
		}
	}

	void initializeAnnotations() {
		if (annotations == null) {
			annotations = new HashSet<AnnotationMirror>();
			copyAnnotations();
		}
	}
	
	public Set<AnnotationMirror> getAnnotations() {
		initializeAnnotations();
		return Collections.unmodifiableSet(annotations);
	};
	
	void clone(AnnotationHolderDelegate annotationHolderDelegate) {
		if (getAnnotations() != null) {
			annotationHolderDelegate.annotations = new HashSet<AnnotationMirror>();
			for (AnnotationMirror annotationMirror: getAnnotations()) {
				annotationHolderDelegate.annotations.add(annotationMirror);
			}
		}
	}

	public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
    	return new AnnotationAccessor(processingEnv) {

			@Override
			public boolean isValid() {
				return false;
			}
    		
			
			public A getAnnotation() {
				return super.getAnnotation(AnnotationHolderDelegate.this, annotationType);
			}
    	}.getAnnotation();
    }
}