package sk.seges.acris.test.server.spring.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import sk.seges.acris.core.server.spring.configuration.AbstractGWTServiceConfiguration;
import sk.seges.acris.rpc.CustomPolicyRPCServiceExporter;
import sk.seges.acris.rpc.RemoteContextSerializationPolicy;
import sk.seges.acris.test.server.service.CardPayService;
import sk.seges.acris.test.shared.service.CardPayRemoteService;
import sk.seges.corpis.domain.pay.tatra.CardPaySettings;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ServicesConfiguration extends AbstractGWTServiceConfiguration {

	@Autowired
	private ServletContext servletContext;

	private @Value("${cardpay.key}") String cardPayKey;

	@Bean
	public RemoteContextSerializationPolicy remoteContextPolicy() {
		return new RemoteContextSerializationPolicy();
	}

	@Bean
	public CardPaySettings cardPaySettings() {
		CardPaySettings cardPaySettings = new CardPaySettings();
		cardPaySettings.setKey(cardPayKey);
		return cardPaySettings;
	}
	
	@Bean
	public CardPayRemoteService cardPayService() {
		return new CardPayService(cardPaySettings());
	}
	
	@Bean
	public CustomPolicyRPCServiceExporter cardPayServiceExporter() throws ServletException {
		CustomPolicyRPCServiceExporter result = registerGWTService(cardPayService(), CardPayRemoteService.class);
		result.setServletContext(servletContext);
		return result;
	}

	@Bean
	public SimpleUrlHandlerMapping urlMapping() throws ServletException {
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		Map<String, RemoteServiceServlet> mappings = new HashMap<String, RemoteServiceServlet>();
		
		mappings.put("/cardpay", cardPayServiceExporter());
		simpleUrlHandlerMapping.setUrlMap(mappings);
		return simpleUrlHandlerMapping;
	}

	@Override
	protected RemoteContextSerializationPolicy getSerializationPolicy() {
		return remoteContextPolicy();
	}
}