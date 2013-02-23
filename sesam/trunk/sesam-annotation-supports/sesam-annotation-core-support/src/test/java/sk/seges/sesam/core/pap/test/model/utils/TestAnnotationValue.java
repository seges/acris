package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;

public class TestAnnotationValue implements AnnotationValue {

	private Object object;
	
	public TestAnnotationValue(Object object) {
		this.object = object;
	}
	
	@Override
	public Object getValue() {
		return object;
	}

	@Override
	public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
		//TODO
		return null;
	}

}
