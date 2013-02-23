package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

abstract class MutableHasAnnotationsType extends MutableType implements HasAnnotations {

	protected final AnnotationHolderDelegate annotationHolderDelegate;
	protected final MutableProcessingEnvironment processingEnv;

	MutableHasAnnotationsType(MutableProcessingEnvironment processingEnv, TypeMirror type) {
		this.processingEnv = processingEnv;
		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, ((DeclaredType)type).asElement());
		} else {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv);
		}
	}

	MutableHasAnnotationsType(MutableProcessingEnvironment processingEnv, MutableTypeMirror type) {
		this.processingEnv = processingEnv;
		if (type != null && type instanceof HasAnnotations) {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, (HasAnnotations)type);
		} else {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv);
		}
	}

    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
    	return annotationHolderDelegate.getAnnotation(annotationType);
    }

	public HasAnnotations annotateWith(AnnotationMirror annotation) {
		//TODO TODO!!!!!!!
		//dirty();
		annotationHolderDelegate.annotateWith(annotation);
		return this;
	}

	public Set<AnnotationMirror> getAnnotations() {
		return annotationHolderDelegate.getAnnotations();
	}
	
	public HasAnnotations annotateWith(MutableAnnotationMirror mutableAnnotationMirror) {
		annotationHolderDelegate.annotateWith(mutableAnnotationMirror);
		return this;
	}
	
	@Override
	public Set<MutableAnnotationMirror> getMutableAnnotations() {
		return annotationHolderDelegate.getMutableAnnotations();
	}
}