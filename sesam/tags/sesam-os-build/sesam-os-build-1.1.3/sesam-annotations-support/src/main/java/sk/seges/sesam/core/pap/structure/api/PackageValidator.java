package sk.seges.sesam.core.pap.structure.api;

import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;

public interface PackageValidator {

	public interface SubPackageType {
		String getName();
	};
	
	boolean isValid();

	PackageValidator moveTo(SubPackageType subPackageType);

	PackageValidator append(ImplementationType type);

	PackageValidator setType(String type);
}
