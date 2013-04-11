package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.reference.ExecutableElementReference;

class ExecutableReference extends MutableValue implements ExecutableElementReference  {

	private final MutableReferenceType[] references;
	private final MutableExecutableElement executableReference;
	
	public ExecutableReference(MutableExecutableElement executableReference, MutableReferenceType... references) {
		super(executableReference);
		this.references = references;
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

		String result = executableReference.getSimpleName() + "(";
		
		int i = 0;
		for (MutableReferenceType reference: references) {
			if (i > 0) {
				result += ", ";
			}
			if (reference.isInline()) {
				//TODO have to use canonical serializer because i can't add import here - try to pass also fpw here somehow, or import collector 
				result += reference.getReference().toString(ClassSerializer.CANONICAL, typed);
			} else {
				result += reference.toString(serializer, typed);
			}
			i++;
		}
		
		result += ")";
		
		return result;
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