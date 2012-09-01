package sk.seges.acris.generator.server.manager;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.manager.JSONOfflineWebSettingsTest.JSONOfflineWebSettingsTestLoader;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.JSONTestConfiguration;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = JSONOfflineWebSettingsTestLoader.class)
public class JSONOfflineWebSettingsTest extends AbstractProcessorTest {

	static class JSONOfflineWebSettingsTestLoader extends ParametrizedAnnotationConfigContextLoader {
		public JSONOfflineWebSettingsTestLoader() {
			super(JSONTestConfiguration.class);
		}
	}

	@Autowired
	private ParametersManagerFactory parameterManagerFactory;

	protected WebSettingsData getWebSettings() {
		WebSettingsData webSettings = new JpaWebSettings();
		String parameters = "{\"offlinePostProcessorInactive\":null,\"offlineIndexProcessorInactive\":[\"NocacheScriptPostProcessor\",\"PropertiesScriptPostProcessor\"],\"offlineAutodetectMode\":false,\"publishOnSaveEnabled\":true}";
		webSettings.setParameters(parameters);
		return webSettings;
	}

	@Test
	@DirtiesContext
	public void testOfflineSettings() {
		JSONOfflineWebSettings offlineWebSettings = new JSONOfflineWebSettings(getWebSettings(), parameterManagerFactory);
		Assert.assertEquals("There should be no processors in the result.", 0, offlineWebSettings.getInactiveProcessors().size());
		Assert.assertEquals("There should be 2 processors in the result.", 2, offlineWebSettings.getInactiveIndexProcessors().size());
		Iterator<String> iterator = offlineWebSettings.getInactiveIndexProcessors().iterator();
		Assert.assertEquals("Wrong processor is defined", "PropertiesScriptPostProcessor", iterator.next());
		Assert.assertEquals("Wrong processor is defined", "NocacheScriptPostProcessor", iterator.next());
	}
}
