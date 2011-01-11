package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.model.InputClass.HasTypeParameters;
import sk.seges.sesam.core.pap.model.TypedClass.TypeParameter;

public interface MutableType extends NamedType {

	HasTypeParameters addType(TypeParameter typeParameter);

	MutableType addClassSufix(String sufix);

	MutableType addClassPrefix(String prefix);

	MutableType addPackageSufix(String sufix);

	MutableType changePackage(String packageName);

}
