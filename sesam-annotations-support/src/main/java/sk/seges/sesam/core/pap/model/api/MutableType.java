package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.structure.api.PackageValidator;


public interface MutableType extends NamedType {

	HasTypeParameters addType(TypeParameter typeParameter);

	MutableType setName(String name);
	
	MutableType addClassSufix(String sufix);

	MutableType addClassPrefix(String prefix);

	MutableType addPackageSufix(String sufix);

	MutableType changePackage(String packageName);
	MutableType changePackage(PackageValidator packageValidator);
}