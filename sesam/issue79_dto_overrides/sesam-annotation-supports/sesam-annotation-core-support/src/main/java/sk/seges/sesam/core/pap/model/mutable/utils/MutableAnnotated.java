package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;

abstract class MutableAnnotated extends MutableType implements HasAnnotations {

	protected final AnnotationHolderDelegate annotationHolderDelegate;
	protected final MutableProcessingEnvironment processingEnv;

	MutableAnnotated(MutableProcessingEnvironment processingEnv, TypeMirror type) {
		this.processingEnv = processingEnv;
		if (type != null && type.getKind().equals(TypeKind.DECLARED)) {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, ((DeclaredType)type).asElement());
		} else {
			this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv);
		}
	}

    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
    	return annotationHolderDelegate.getAnnotation(annotationType);
    }

	public void annotateWith(AnnotationMirror annotation) {
		//TODO TODO!!!!!!!
		//dirty();
		annotationHolderDelegate.annotateWith(annotation);
	}

	public Set<AnnotationMirror> getAnnotations() {
		return annotationHolderDelegate.getAnnotations();
	}
}