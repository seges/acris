package sk.seges.acris.core.server.spring.configuration;

import javax.servlet.ServletContext;

import sk.seges.acris.rpc.CustomPolicyRPCServiceExporter;
import sk.seges.acris.rpc.RemoteContextSerializationPolicy;

import com.google.gwt.user.client.rpc.RemoteService;

public abstract class AbstractGWTServiceConfiguration {

	protected abstract RemoteContextSerializationPolicy getSerializationPolicy();

	protected CustomPolicyRPCServiceExporter registerLocalExporter(ServletContext servletContext, RemoteService service, Class<? extends RemoteService> serviceInterface) {
		CustomPolicyRPCServiceExporter exporter = registerGWTService(service, serviceInterface);
		exporter.setServletContext(servletContext);
		return exporter;
	}

	@SuppressWarnings("unchecked")
	protected CustomPolicyRPCServiceExporter registerGWTService(Object service,
			Class<? extends RemoteService> serviceInterface) {
		CustomPolicyRPCServiceExporter exporter = new CustomPolicyRPCServiceExporter();
		exporter.setSerializationPolicy(getSerializationPolicy());
		exporter.setService(service);
		exporter.setServiceInterfaces(new Class[] { serviceInterface });
		return exporter;
	}
}
