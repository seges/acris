package sk.seges.sesam.core.pap.test.model.utils;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;

public class TestWildcardType extends TestTypeMirror implements WildcardType {

	private TypeMirror extendsBound = null;
	private TypeMirror superBound = null;
	
	public TestWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
		super(TypeKind.WILDCARD);
		this.superBound = superBound;
		this.extendsBound = extendsBound;
	}

	@Override
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		return v.visitWildcard(null, p);
	}

	@Override
	public TypeMirror getExtendsBound() {
		return extendsBound;
	}

	@Override
	public TypeMirror getSuperBound() {
		return superBound;
	}
}