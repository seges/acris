package sk.seges.sesam.core.pap.builder.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;

public interface TypeMirrorConverter {

	NamedType handleType(TypeMirror type);
}
