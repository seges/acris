package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class TestVariableElement extends TestElement implements VariableElement {

	private final TypeVariable<Method> typeVariable;
	private final Class<?> parameterType;
	
	public TestVariableElement(TypeVariable<Method> typeVariable, Class<?> parameterType) {
		super(ElementKind.PARAMETER);
		this.typeVariable = typeVariable;
		this.parameterType = parameterType;
	}

	@Override
	public TypeMirror asType() {
		if (parameterType.isPrimitive()) {
			return new TestPrimitiveType(parameterType);
		}
		
		return new TestTypeElement(parameterType).asType();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		// TODO Auto-generated method stub
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
		return new HashSet<Modifier>();
	}

	@Override
	public Name getSimpleName() {
		return new TestName(typeVariable.getName());
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
		return v.visit(this, p);
	}

	@Override
	public Object getConstantValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
