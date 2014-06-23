package sk.seges.acris.rpc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Factory for RPC Service exporters able to create custom service exporters.
 * Exporter class is retrieved from Spring application context using service
 * exporter bean name. Default service exporter bean name is defined in constant
 * DEFAULT_SERVICE_EXPORTER_BEAN_NAME.
 * 
 * @author mig
 *
 */
public class ConfigurableRPCServiceExporterFactory implements RPCServiceExporterFactory, ApplicationContextAware {
	/** Default service exporter bean name that should be present in application context. Usually it is prototype bean. */
	private static final String DEFAULT_SERVICE_EXPORTER_BEAN_NAME = "serviceExporter";
	
	private ApplicationContext ctx;
	private String serviceExporterBeanName = DEFAULT_SERVICE_EXPORTER_BEAN_NAME;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}
	
	public void setServiceExporterBeanName(String serviceExporterBeanName) {
		this.serviceExporterBeanName = serviceExporterBeanName;
	}
	
	@Override
	public RPCServiceExporter create() {
		return (RPCServiceExporter) ctx.getBean(serviceExporterBeanName);
	}
}
