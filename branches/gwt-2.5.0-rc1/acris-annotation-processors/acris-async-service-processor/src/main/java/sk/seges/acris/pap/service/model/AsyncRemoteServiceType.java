package sk.seges.acris.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.acris.pap.service.async.accessor.RemoteServiceAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

public class AsyncRemoteServiceType extends DelegateMutableDeclaredType {

	public static final String ASYNC_SUFFIX = "Async";

	private RemoteServiceTypeElement remoteService;
	
	public AsyncRemoteServiceType(RemoteServiceTypeElement remoteService, MutableProcessingEnvironment processingEnv) {
		this.remoteService = remoteService;
		
		setKind(MutableTypeKind.INTERFACE);	

		List<? extends MutableTypeMirror> interfaces = remoteService.getInterfaces();
		List<MutableTypeMirror> asyncInterfaces = new ArrayList<MutableTypeMirror>();
		
		if (interfaces != null && interfaces.size() > 0) {
			for (MutableTypeMirror interfaceType: interfaces) {
				if (interfaceType.getKind().equals(MutableTypeKind.INTERFACE)) {
					if (new RemoteServiceAccessor((MutableDeclaredType)interfaceType, processingEnv).isValid()) {
						asyncInterfaces.add(new AsyncRemoteServiceType(
								new RemoteServiceTypeElement((TypeElement) ((DeclaredType)processingEnv.getTypeUtils().fromMutableType(interfaceType)).asElement(), 
										processingEnv), processingEnv));
					}
				}
			}
		}
		
		setInterfaces(asyncInterfaces);
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return remoteService.clone().addClassSufix(ASYNC_SUFFIX).cloneTypeVariables(remoteService);
	}
}
