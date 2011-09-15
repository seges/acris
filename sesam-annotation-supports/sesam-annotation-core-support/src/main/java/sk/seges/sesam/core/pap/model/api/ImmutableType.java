package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.structure.api.PackageValidator;


public interface ImmutableType extends NamedType {

	HasTypeParameters addType(TypeParameter typeParameter);
	ImmutableType setEnclosedClass(NamedType type);

	ImmutableType setName(String name);
	
	ImmutableType addClassSufix(String sufix);

	ImmutableType addClassPrefix(String prefix);

	ImmutableType addPackageSufix(String sufix);

	ImmutableType changePackage(String packageName);
	ImmutableType changePackage(PackageValidator packageValidator);
}