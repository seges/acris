package sk.seges.sesam.core.pap.test.model.utils;

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
import javax.lang.model.type.TypeMirror;

class TestTypeElement extends TestElement implements TypeElement {

	private final String simpleName;
	private final String packageName;
	
	public TestTypeElement(ElementKind kind, String simpleName, String packageName) {
		super(kind);
		this.simpleName = simpleName;
		this.packageName = packageName;
	}
	
	@Override
	public TypeMirror asType() {
		return new TestDeclaredType(this);
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		return null;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public String toString() {
		return packageName + "." + simpleName;
	}
	
	TestPackageElement getPackage() {
		return new TestPackageElement(packageName);
	}
}
