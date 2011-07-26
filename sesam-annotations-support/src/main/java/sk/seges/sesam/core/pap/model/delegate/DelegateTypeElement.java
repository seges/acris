package sk.seges.sesam.core.pap.model.delegate;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;

public class DelegateTypeElement implements TypeElement {

	private VariableElement field;
	private TypeElement typeElement;
	private ProcessorConfigurer configurer;
	
	public DelegateTypeElement(VariableElement field, ProcessorConfigurer configurer) {
		if (!field.getKind().equals(TypeKind.DECLARED)) {
			throw new RuntimeException("Only declared types are supported in the processing. Invalid type " + field.asType() + " in the field " + field.toString());
		}
		this.configurer = configurer;
		this.typeElement = (TypeElement)((DeclaredType)field.asType()).asElement();
		this.field = field;
	}
	
	@Override
	public TypeMirror asType() {
		return typeElement.asType();
	}

	@Override
	public ElementKind getKind() {
		return typeElement.getKind();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		//TODO filter just supported
		//Convert delegates
		return field.getAnnotationMirrors();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		//Convert delegates
		return field.getAnnotation(annotationType);
	}

	@Override
	public Set<Modifier> getModifiers() {
		return typeElement.getModifiers();
	}

	@Override
	public Name getSimpleName() {
		return typeElement.getSimpleName();
	}

	@Override
	public Element getEnclosingElement() {
		return typeElement.getEnclosingElement();
	}

	@Override
	public List<? extends Element> getEnclosedElements() {
		return typeElement.getEnclosedElements();
	}

	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p) {
		return typeElement.accept(v, p);
	}

	@Override
	public NestingKind getNestingKind() {
		return typeElement.getNestingKind();
	}

	@Override
	public Name getQualifiedName() {
		return typeElement.getQualifiedName();
	}

	@Override
	public TypeMirror getSuperclass() {
		return typeElement.getSuperclass();
	}

	@Override
	public List<? extends TypeMirror> getInterfaces() {
		return typeElement.getInterfaces();
	}

	@Override
	public List<? extends TypeParameterElement> getTypeParameters() {
		return typeElement.getTypeParameters();
	}
}