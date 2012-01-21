package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
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
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.test.TestAnnotationMirror;

class TestTypeElement extends TestElement implements TypeElement {

	private final String simpleName;
	private final String packageName;
	
	private List<AnnotationMirror> annotationMirrors;
	private Annotation[] annotations;

	private Class<?>[] interfaces;
	private Class<?> superclass;
	private Class<?> enclosingClass;

	public TestTypeElement(Class<?> clazz) {
		super(clazz.isInterface() ? ElementKind.INTERFACE: clazz.isAnnotation() ? ElementKind.ANNOTATION_TYPE:
			  clazz.isEnum() ? ElementKind.ENUM: ElementKind.CLASS);
		this.simpleName = clazz.getSimpleName();
		this.packageName = clazz.getPackage().getName().toString();
		
		annotations = clazz.getAnnotations();
		interfaces = clazz.getInterfaces();
		
		superclass = clazz.getSuperclass();
		enclosingClass = clazz.getEnclosingClass();
		
		//TODO type variables
	}
	
	@Override
	public TypeMirror asType() {
		return new TestDeclaredType(this);
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		if (annotationMirrors == null) {
			annotationMirrors = new LinkedList<AnnotationMirror>();
			
			for (Annotation annotation: annotations) {
				TestAnnotationMirror testAnnotationMirror = new TestAnnotationMirror(new TestDeclaredType(new TestTypeElement(annotation.annotationType())), annotation);
				annotationMirrors.add(testAnnotationMirror);
			}
		}
		return annotationMirrors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		for (Annotation annotation: annotations) {
			if (annotation.annotationType().getName().toString().equals(annotationType.getName())) {
				return (A) annotation;
			}
		}
		return null;
	}

	@Override
	public Set<Modifier> getModifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name getSimpleName() {
		return new TestName(simpleName);
	}

	@Override
	public Element getEnclosingElement() {
		if (enclosingClass != null) {
			return new TestTypeElement(enclosingClass);
		}
		
		return null;
	}

	@Override
	public List<? extends Element> getEnclosedElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p) {
		return v.visitType(this, p);
	}

	@Override
	public NestingKind getNestingKind() {
		return null;
	}

	@Override
	public Name getQualifiedName() {
		//TODO nested types support
		return new TestName(packageName + "." + simpleName);
	}

	@Override
	public TypeMirror getSuperclass() {
		if (superclass != null) {
			return new TestTypeElement(superclass).asType();
		}
		
		return new TestNoneType();
	}

	@Override
	public List<? extends TypeMirror> getInterfaces() {
		if (interfaces == null || interfaces.length == 0) {
			return new ArrayList<TypeMirror>();
		}
		List<TypeMirror> result = new ArrayList<TypeMirror>();
		for (Class<?> interfaceClass: interfaces) {
			result.add(new TestTypeElement(interfaceClass).asType());
		}
		return result;
	}

	@Override
	public List<? extends TypeParameterElement> getTypeParameters() {
		//TODO
		return new LinkedList<TypeParameterElement>();
	}

	@Override
	public String toString() {
		return packageName + "." + simpleName;
	}
	
	TestPackageElement getPackage() {
		return new TestPackageElement(packageName);
	}
}