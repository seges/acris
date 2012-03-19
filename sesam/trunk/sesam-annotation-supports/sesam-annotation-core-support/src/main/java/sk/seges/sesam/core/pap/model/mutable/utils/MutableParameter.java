package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;

class MutableParameter implements MutableVariableElement {

	private final MutableProcessingEnvironment processingEnv;
	
	public MutableParameter(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	@Override
	public MutableTypeMirror asType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableElementKind getKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Modifier> getModifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableElement getEnclosingElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MutableElement> getEnclosedElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

}
