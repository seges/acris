package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.reference.ExecutableElementReference;

class ExecutableReference extends MutableValue implements ExecutableElementReference  {

	private final MutableReferenceType reference;
	private final MutableExecutableElement executableReference;
	
	public ExecutableReference(MutableExecutableElement executableReference, MutableReferenceType reference) {
		super(reference);
		this.reference = reference;
		this.executableReference = executableReference;
	}
	
	@Override
	public String toString() {
		return toString(ClassSerializer.CANONICAL);
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return toString(serializer, true);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		if (executableReference == null) {
			return "null";
		}

		return executableReference.getSimpleName() + "(" + reference.toString(serializer, typed) + ")";
	}

	@Override
	public MutableDeclaredType asType() {
		//TODO only declared types are supported
		//TODO add TypeVariables, arrays, etc
		return (MutableDeclaredType) executableReference.asType().getReturnType();
	}

	@Override
	public MutableExecutableElement getReference() {
		return executableReference;
	}
}