package sk.seges.sesam.core.pap.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.test.model.utils.TestAnnotationValue;
import sk.seges.sesam.core.pap.test.model.utils.TestExecutableElement;

public class TestAnnotationMirror implements AnnotationMirror {

	private final DeclaredType type;
	private final Annotation annotation;
	
	private Map<ExecutableElement, AnnotationValue> elementValues;
	
	public TestAnnotationMirror(DeclaredType type, Annotation annotation) {
		this.type = type;
		this.annotation = annotation;
	}
	
	@Override
	public DeclaredType getAnnotationType() {
		return type;
	}

	@Override
	public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValues() {
		if (elementValues != null) {
			return elementValues;
		}
		
		Method[] methods = annotation.annotationType().getMethods();
		
		elementValues = new HashMap<ExecutableElement, AnnotationValue>();
		
		for (Method method: methods) {
			elementValues.put(new TestExecutableElement(method), new TestAnnotationValue(method.getDefaultValue()));
		}
		
		return elementValues;
	}
}
