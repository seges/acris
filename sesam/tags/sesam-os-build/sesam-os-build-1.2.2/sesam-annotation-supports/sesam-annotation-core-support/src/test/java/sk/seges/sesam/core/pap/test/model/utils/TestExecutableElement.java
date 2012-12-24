package sk.seges.sesam.core.pap.test.model.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.utils.PAPReflectionUtils;

public class TestExecutableElement extends TestElement implements ExecutableElement {

	private TypeMirror type;
	private final Method method;
	
	private Set<Modifier> modifiers;
	private List<VariableElement> parameters;
	
	public TestExecutableElement(Method method) {
		super(ElementKind.METHOD);
		this.method = method;
	}

	@Override
	public TypeMirror asType() {
		if (type == null) {
			type = new TestExecutableType(this);
		}
		
		return type;
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
		if (modifiers != null) {
			return modifiers;
		}
		
		return modifiers = PAPReflectionUtils.toModifiers(method.getModifiers());
	}

	@Override
	public Name getSimpleName() {
		return new TestName(method.getName());
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
		return v.visitExecutable(this, p);
	}

	@Override
	public List<? extends TypeParameterElement> getTypeParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeMirror getReturnType() {
		if (method.getReturnType().isPrimitive()) {
			return new TestPrimitiveType(method.getReturnType());
		}
		
		return new TestTypeElement(method.getReturnType()).asType();
	}

	@Override
	public List<? extends VariableElement> getParameters() {
		
		if (parameters != null) {
			return parameters;
		}
		
		TypeVariable<Method>[] typeParameters = method.getTypeParameters();
		
		parameters = new ArrayList<VariableElement>();
		int i = 0;
		
		for (TypeVariable<Method> typeParameter: typeParameters) {
			parameters.add(new TestVariableElement(typeParameter, method.getParameterTypes()[i++]));
		}
		
		return parameters;
	}

	@Override
	public boolean isVarArgs() {
		return method.isVarArgs();
	}

	@Override
	public List<? extends TypeMirror> getThrownTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationValue getDefaultValue() {
		return new TestAnnotationValue(method.getDefaultValue());
	}
}