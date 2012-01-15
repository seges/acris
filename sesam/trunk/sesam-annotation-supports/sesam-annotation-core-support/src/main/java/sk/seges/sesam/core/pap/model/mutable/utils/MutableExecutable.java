package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;

class MutableExecutable extends MutableType implements MutableExecutableType {

	protected String simpleName;

	private final AnnotationHolderDelegate annotationHolderDelegate;
	private final ExecutableElement executableElement;
	private final MutableProcessingEnvironment processingEnv;
	
	MutableExecutable(ExecutableElement executableElement, MutableProcessingEnvironment processingEnv) {
		this.executableElement = executableElement;
		this.processingEnv = processingEnv;
		this.simpleName = executableElement.getSimpleName().toString();
		this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, executableElement);
	}
	
	@Override
	public MutableTypeKind getKind() {
		return MutableTypeKind.METHOD;
	}

	@Override
	public String toString() {
		return simpleName;
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return simpleName;
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return simpleName;
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

	@Override
	public MutableExecutableType clone() {
		MutableExecutable result = new MutableExecutable(executableElement, processingEnv);
		
		annotationHolderDelegate.clone(result.annotationHolderDelegate);

		return result;
	}
}