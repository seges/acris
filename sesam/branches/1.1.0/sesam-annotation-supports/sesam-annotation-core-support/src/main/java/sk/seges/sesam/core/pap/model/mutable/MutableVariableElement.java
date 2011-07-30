package sk.seges.sesam.core.pap.model.mutable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

public class MutableVariableElement implements Element {

	private Element element;
	private Name simpleName;
	private ProcessingEnvironment pe;
	
	public MutableVariableElement(Element element, ProcessingEnvironment pe) {
		this.element = element;
		this.pe = pe;
		this.simpleName = element.getSimpleName();
	}

	@Override
	public TypeMirror asType() {
		return element.asType();
	}

	@Override
	public ElementKind getKind() {
		return element.getKind();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		return element.getAnnotationMirrors();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		return element.getAnnotation(annotationType);
	}

	@Override
	public Set<Modifier> getModifiers() {
		return element.getModifiers();
	}

	@Override
	public Name getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String name) {
		this.simpleName = pe.getElementUtils().getName(name);
	}
	
	@Override
	public Element getEnclosingElement() {
		return element.getEnclosingElement();
	}

	@Override
	public List<? extends Element> getEnclosedElements() {
		return element.getEnclosedElements();
	}

	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p) {
		return element.accept(v, p);
	}
}