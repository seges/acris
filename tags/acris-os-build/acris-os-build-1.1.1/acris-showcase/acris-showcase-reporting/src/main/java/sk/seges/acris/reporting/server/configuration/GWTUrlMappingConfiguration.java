package sk.seges.acris.reporting.server.configuration;

import java.util.Properties;

import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import sk.seges.acris.reporting.rpc.domain.twig.TwigReportDescription;
import sk.seges.acris.reporting.rpc.domain.twig.TwigReportParameter;
import sk.seges.acris.reporting.server.dao.api.IReportDescriptionDao;
import sk.seges.acris.reporting.server.dao.api.IReportParameterDao;
import sk.seges.acris.reporting.server.dao.twig.ReportDescriptionTwigDao;
import sk.seges.acris.reporting.server.dao.twig.ReportParameterTwigDao;
import sk.seges.acris.reporting.server.service.MockReportingService;
import sk.seges.acris.reporting.server.service.ReportDescriptionService;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.acris.reporting.shared.service.IReportingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

@Configuration
public class GWTUrlMappingConfiguration {
	@Bean
	public HandlerMapping urlMapping() {
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		Properties mappings = new Properties();
		mappings.put("/reportDescriptionService", "reportDescriptionService");
		mappings.put("/reportingService", "reportingService");
		simpleUrlHandlerMapping.setMappings(mappings);
		return simpleUrlHandlerMapping;
	}

	@Bean
	public ObjectDatastore objectDatastore() {
		return new AnnotationObjectDatastore();
	}

	@Bean
	public IReportDescriptionDao<ReportDescriptionData> reportDescriptionDao() {
		return new ReportDescriptionTwigDao(objectDatastore(), TwigReportDescription.class);
	}

	@Bean
	public IReportParameterDao<ReportParameterData> reportParameterDao() {
		return new ReportParameterTwigDao(objectDatastore(), TwigReportParameter.class);
	}

	@SuppressWarnings("unchecked")
	@Bean
	public RemoteServiceServlet reportDescriptionService() {
		GWTRPCServiceExporter exporter = new GWTRPCServiceExporter();
		exporter.setService(reportDescriptionOnlyService());
		exporter.setServiceInterfaces(new Class[] { IReportDescriptionService.class });
		return exporter;
	}

	@Bean
	public IReportDescriptionService reportDescriptionOnlyService() {
		return new ReportDescriptionService(this.reportDescriptionDao(), this.reportParameterDao());
	}

	@SuppressWarnings("unchecked")
	@Bean
	public RemoteServiceServlet reportingService() {
		GWTRPCServiceExporter exporter = new GWTRPCServiceExporter();
		exporter.setService(new MockReportingService(this.reportDescriptionOnlyService()));
		exporter.setServiceInterfaces(new Class[] { IReportingService.class });
		return exporter;
	}
}
