package sk.seges.sesam.core.pap.structure.api;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

public interface PackageValidatorProvider {

	PackageValidator get(String packageName);
	
	PackageValidator get(MutableDeclaredType inputClass);
}