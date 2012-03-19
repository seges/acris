package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

public class TestArrayType extends TestTypeMirror implements ArrayType {

	private final TypeMirror componentType;
	
	public TestArrayType(TypeMirror componentType) {
		super(TypeKind.ARRAY);
		this.componentType = componentType;
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitArray(this, p);
	}

	@Override
	public TypeMirror getComponentType() {
		return componentType;
	}
}
