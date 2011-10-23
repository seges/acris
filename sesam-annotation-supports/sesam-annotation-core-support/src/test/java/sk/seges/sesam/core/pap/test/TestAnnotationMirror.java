package sk.seges.sesam.core.pap.test;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;

public class TestAnnotationMirror implements AnnotationMirror {

	private DeclaredType type;
	
	public TestAnnotationMirror(DeclaredType type) {
		this.type = type;
	}
	
	@Override
	public DeclaredType getAnnotationType() {
		return type;
	}

	@Override
	public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValues() {
		return null;
	}
}
