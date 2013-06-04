package sk.seges.acris.pap.service.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

public class AsyncRemoteServiceType extends DelegateMutableDeclaredType {

	public static final String ASYNC_SUFFIX = "Async";

	private RemoteServiceTypeElement remoteService;
	
	public AsyncRemoteServiceType(RemoteServiceTypeElement remoteService) {
		this.remoteService = remoteService;
		
		setKind(MutableTypeKind.INTERFACE);	
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return remoteService.clone().addClassSufix(ASYNC_SUFFIX).cloneTypeVariables(remoteService);
	}
}
