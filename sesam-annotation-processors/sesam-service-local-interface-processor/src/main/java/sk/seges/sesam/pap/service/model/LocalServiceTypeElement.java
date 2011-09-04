package sk.seges.sesam.pap.service.model;

import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public class LocalServiceTypeElement extends DelegateImmutableType {

	public static final String REMOTE_SUFFIX = "Remote";
	public static final String LOCAL_SUFFIX = "Local";

	private final boolean isGenerated;
	
	private final RemoteServiceTypeElement remoteService;
	
	public LocalServiceTypeElement(RemoteServiceTypeElement remoteService) {
		this.isGenerated = true;
		this.remoteService = remoteService;
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return getLocalServiceClass(remoteService);
	}
	
	public ImmutableType getLocalServiceClass(ImmutableType mutableType) {
		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(REMOTE_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - REMOTE_SUFFIX.length());
		}
		PackageValidator packageValidator = new DefaultPackageValidatorProvider().get(mutableType.getPackageName());
		packageValidator.moveTo(LocationType.SERVER);
		mutableType = mutableType.changePackage(packageValidator);
		return mutableType.setName(simpleName + LOCAL_SUFFIX);
	}

	public boolean isGenerated() {
		return isGenerated;
	}
	
	public RemoteServiceTypeElement getRemoteService() {
		return remoteService;
	}
}