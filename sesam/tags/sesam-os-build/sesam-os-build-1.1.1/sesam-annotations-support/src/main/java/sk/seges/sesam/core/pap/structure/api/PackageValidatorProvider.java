package sk.seges.sesam.core.pap.structure.api;

import sk.seges.sesam.core.pap.model.api.NamedType;

public interface PackageValidatorProvider {

	PackageValidator get(String packageName);
	
	PackageValidator get(NamedType inputClass);
}
