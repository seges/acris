package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
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

	public TestTypeElement(Class<?> clazz) {
		super(clazz.isInterface() ? ElementKind.INTERFACE: clazz.isAnnotation() ? ElementKind.ANNOTATION_TYPE:
			  clazz.isEnum() ? ElementKind.ENUM: ElementKind.CLASS);
		this.simpleName = clazz.getSimpleName();
		this.packageName = clazz.getPackage().getName().toString();
		
		annotations = clazz.getAnnotations();
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
				annotationMirrors.add(new TestAnnotationMirror(new TestDeclaredType(new TestTypeElement(annotation.getClass()))));
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends TypeMirror> getInterfaces() {
		// TODO Auto-generated method stub
		return null;
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