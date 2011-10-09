package sk.seges.sesam.pap.service.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class ServiceConverterTypeElement extends DelegateMutableDeclaredType {

	public static final String SERVICE_CONVERTER_SUFFIX = "Converter";

	private final ServiceTypeElement serviceTypeElement;
	
	ServiceConverterTypeElement(ServiceTypeElement serviceTypeElement) {
		this.serviceTypeElement = serviceTypeElement;

		List<LocalServiceTypeElement> localServiceInterfaces = serviceTypeElement.getLocalServiceInterfaces();

		if (localServiceInterfaces != null && localServiceInterfaces.size() > 0) {
			Set<MutableDeclaredType> interfaces = new HashSet<MutableDeclaredType>();

			for (LocalServiceTypeElement localInterface : localServiceInterfaces) {
				interfaces.add(localInterface.getRemoteServiceInterface());
			}

			setInterfaces(interfaces);
		}
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return serviceTypeElement.clone().addClassSufix(SERVICE_CONVERTER_SUFFIX);
	}
}