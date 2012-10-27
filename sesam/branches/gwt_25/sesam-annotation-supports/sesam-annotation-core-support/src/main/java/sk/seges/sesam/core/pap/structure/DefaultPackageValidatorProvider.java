package sk.seges.sesam.core.pap.structure;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public class DefaultPackageValidatorProvider implements PackageValidatorProvider {
 
	@Override
	public PackageValidator get(String packageName) {
		return new DefaultPackageValidator(packageName);
	}

	@Override
	public PackageValidator get(MutableDeclaredType inputClass) {
		return new DefaultPackageValidator(inputClass);
	}	
}