package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;

abstract class MutableHasAnnotationsElement extends MutableElement implements HasAnnotations {

	protected final AnnotationHolderDelegate annotationHolderDelegate;

	protected MutableHasAnnotationsElement(MutableElementKind kind, MutableTypeMirror type, String name, MutableProcessingEnvironment processingEnv) {
		super(kind, type, name, processingEnv);
		annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv);
	}

	@Override
	public HasAnnotations annotateWith(AnnotationMirror annotationMirror) {
		annotationHolderDelegate.annotateWith(annotationMirror);
		return this;
	}

	@Override
	public HasAnnotations annotateWith(MutableAnnotationMirror mutableAnnotationMirror) {
		annotationHolderDelegate.annotateWith(mutableAnnotationMirror);
		return this;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		return annotationHolderDelegate.getAnnotation(annotationType);
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return annotationHolderDelegate.getAnnotations();
	}

	@Override
	public Set<MutableAnnotationMirror> getMutableAnnotations() {
		return annotationHolderDelegate.getMutableAnnotations();
	}
}