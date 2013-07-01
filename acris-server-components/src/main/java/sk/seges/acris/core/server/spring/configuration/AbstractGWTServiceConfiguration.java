package sk.seges.acris.core.server.spring.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import sk.seges.acris.rpc.CustomPolicyRPCServiceExporter;
import sk.seges.acris.rpc.RemoteContextSerializationPolicy;

import com.google.gwt.user.client.rpc.RemoteService;

public abstract class AbstractGWTServiceConfiguration {

	public RemoteContextSerializationPolicy getSerializationPolicy() {
		return new RemoteContextSerializationPolicy();
	}

	protected CustomPolicyRPCServiceExporter registerLocalExporter(ServletContext servletContext, RemoteService service, Class<? extends RemoteService> serviceInterface) {
		CustomPolicyRPCServiceExporter exporter = registerGWTService(service, serviceInterface);
		exporter.setServletContext(servletContext);
		return exporter;
	}

	@SuppressWarnings("unchecked")
	protected CustomPolicyRPCServiceExporter registerGWTService(Object service, Class<? extends RemoteService> serviceInterface) {
		return registerGWTServices(service, serviceInterface);
	}

	@SuppressWarnings("unchecked")
	protected CustomPolicyRPCServiceExporter registerGWTServices(Object service,
			Class<? extends RemoteService> ...serviceInterfaces) {

		if (serviceInterfaces != null && serviceInterfaces.length > 0) {

			List<Class<? extends RemoteService>> interfaces = new ArrayList<Class<? extends RemoteService>>();

			for (Class<? extends RemoteService> serviceInterface: serviceInterfaces) {
				if (serviceInterface != null) {
					if (!serviceInterface.isAssignableFrom(service.getClass())) {
						throw new RuntimeException("Service " + service.getClass() + " should implement " + serviceInterface.getName());
					}
					interfaces.add(serviceInterface);
				}
			}

			if (interfaces.size() > 0) {
				CustomPolicyRPCServiceExporter exporter = new CustomPolicyRPCServiceExporter();
				exporter.setSerializationPolicy(getSerializationPolicy());
				exporter.setService(service);
				exporter.setServiceInterfaces(interfaces.toArray(new Class[] {}));
//				try {
//					exporter.init();
//				} catch (ServletException e) {
//					throw new RuntimeException(e);
//				}
				return exporter;
			}
		}

		return null;
	}
}