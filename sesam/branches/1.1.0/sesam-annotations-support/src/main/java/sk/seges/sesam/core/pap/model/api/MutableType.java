package sk.seges.sesam.core.pap.model.api;


public interface MutableType extends NamedType {

	HasTypeParameters addType(TypeParameter typeParameter);

	MutableType addClassSufix(String sufix);

	MutableType addClassPrefix(String prefix);

	MutableType addPackageSufix(String sufix);

	MutableType changePackage(String packageName);

}
