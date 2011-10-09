package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

public class TestPackageElement extends TestElement implements PackageElement {

	private String name;
	
	public TestPackageElement(CharSequence name) {
		super(ElementKind.PACKAGE);
		this.name = name.toString();
	}
	
	@Override
	public TypeMirror asType() {
		return new TestPackageType();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		return new ArrayList<AnnotationMirror>();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		return null;
	}

	@Override
	public Set<Modifier> getModifiers() {
		return new HashSet<Modifier>();
	}

	@Override
	public Name getSimpleName() {
		return new TestName(name);
	}

	@Override
	public Element getEnclosingElement() {
		return null;
	}

	@Override
	public List<? extends Element> getEnclosedElements() {
		return null;
	}

	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p) {
		return v.visitPackage(this, p);
	}

	@Override
	public Name getQualifiedName() {
		return null;
	}

	@Override
	public boolean isUnnamed() {
		return false;
	}
}