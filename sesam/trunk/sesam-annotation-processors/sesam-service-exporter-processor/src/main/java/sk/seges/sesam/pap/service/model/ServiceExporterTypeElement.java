package sk.seges.sesam.pap.service.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class ServiceExporterTypeElement extends DelegateMutableDeclaredType {

	private static final String EXPORTER_SUFFIX = "Exporter";

	private final MutableDeclaredType serviceType;
	
	public ServiceExporterTypeElement(MutableDeclaredType serviceType) {
		this.serviceType = serviceType;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return serviceType.clone().addClassSufix(EXPORTER_SUFFIX);
	}

}
