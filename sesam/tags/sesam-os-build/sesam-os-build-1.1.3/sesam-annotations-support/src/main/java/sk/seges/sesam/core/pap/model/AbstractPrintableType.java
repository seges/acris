package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.PrintableType;

public abstract class AbstractPrintableType implements PrintableType, NamedType {

	public String getCanonicalName(NamedType outputName, boolean typed) {
		return toString(outputName, ClassSerializer.CANONICAL, typed);
	}

	public String getSimpleName(NamedType outputName, boolean typed) {
		return toString(outputName, ClassSerializer.SIMPLE, typed);
	}

	public String getQualifiedName(InputClass outputName, boolean typed) {
		return toString(outputName, ClassSerializer.QUALIFIED, typed);
	}

	public String toString(NamedType inputClass, ClassSerializer serializer, boolean typed) {
		if (this.getCanonicalName().equals(NamedType.THIS.getCanonicalName())) {
			return inputClass.toString(serializer);
		}
		return toString(serializer);
	}
	
	public String toString(ClassSerializer serializer) {
		switch (serializer) {
		case CANONICAL:
			return getCanonicalName();
		case QUALIFIED:
			return getQualifiedName();
		case SIMPLE:
			return getSimpleName();
		}
		return null;
	}
}