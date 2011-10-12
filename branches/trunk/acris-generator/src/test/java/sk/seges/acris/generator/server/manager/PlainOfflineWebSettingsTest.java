package sk.seges.acris.generator.server.manager;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.manager.PlainOfflineWebSettingsTest.PlainOfflineWebSettingsTestLoader;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.DefaultTestConfiguration;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = PlainOfflineWebSettingsTestLoader.class)
public class PlainOfflineWebSettingsTest extends AbstractProcessorTest {

	static class PlainOfflineWebSettingsTestLoader extends ParametrizedAnnotationConfigContextLoader {
		public PlainOfflineWebSettingsTestLoader() {
			super(DefaultTestConfiguration.class);
		}
	}

	@Autowired
	private ParametersManagerFactory parameterManagerFactory;
	
	protected WebSettingsData getWebSettings() {
		WebSettingsData webSettings = new WebSettingsDTO();
		String parameters = "offline.post.processor.inactive=;offline.index.post.processor.inactive=NochacheScriptPostProcessor,PropertiesScriptPostProcessor";
		webSettings.setParameters(parameters);
		return webSettings;
	}
	
	@Test
	@DirtiesContext
	public void testOfflineSettings() {
		
		PlainOfflineWebSettings plainOfflineWebSettings = new PlainOfflineWebSettings(getWebSettings(), parameterManagerFactory);
		Assert.assertEquals("There should be no processors in the result.", 1, plainOfflineWebSettings.getInactiveProcessors().size());
		String processor = plainOfflineWebSettings.getInactiveProcessors().iterator().next();
		Assert.assertEquals("No processor should be defined", "", processor);
		Assert.assertEquals("There should be 2 processors in the result.", 2, plainOfflineWebSettings.getInactiveIndexProcessors().size());
		Iterator<String> iterator = plainOfflineWebSettings.getInactiveIndexProcessors().iterator();
		Assert.assertEquals("Wrong processor is defined", "NochacheScriptPostProcessor", iterator.next());
		Assert.assertEquals("Wrong processor is defined", "PropertiesScriptPostProcessor", iterator.next());
	}
}
