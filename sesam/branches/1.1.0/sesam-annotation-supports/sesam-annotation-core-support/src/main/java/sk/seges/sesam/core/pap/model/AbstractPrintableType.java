package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.PrintableType;

public abstract class AbstractPrintableType implements PrintableType, NamedType {

	public String getCanonicalName(boolean typed) {
		return toString(ClassSerializer.CANONICAL, typed);
	}

	public String getSimpleName(boolean typed) {
		return toString(ClassSerializer.SIMPLE, typed);
	}

	public String getQualifiedName(boolean typed) {
		return toString(ClassSerializer.QUALIFIED, typed);
	}

	public String toString(ClassSerializer serializer, boolean typed) {
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