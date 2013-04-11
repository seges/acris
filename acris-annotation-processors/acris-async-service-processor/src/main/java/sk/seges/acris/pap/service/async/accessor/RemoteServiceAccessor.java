package sk.seges.acris.pap.service.async.accessor;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public class RemoteServiceAccessor extends AnnotationAccessor {

	private final RemoteServiceRelativePath remoteServiceRelativePath;
	private final RemoteServicePath remoteServicePath;
	private final RemoteServiceDefinition remoteServiceDefinition;
	
	public RemoteServiceAccessor(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);

		this.remoteServiceRelativePath = getAnnotation(type, RemoteServiceRelativePath.class);
		this.remoteServicePath = getAnnotation(type, RemoteServicePath.class);
		this.remoteServiceDefinition = getAnnotation(type, RemoteServiceDefinition.class);
	}

	@Override
	public boolean isValid() {
		return remoteServiceDefinition != null || remoteServicePath != null || remoteServiceRelativePath != null;
	}
}