package sk.seges.acris.generator.server.manager;

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
import sk.seges.acris.generator.server.spring.configuration.PlainParametersTestConfiguration;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = PlainOfflineWebSettingsTestLoader.class)
public class PlainOfflineWebSettingsTest extends AbstractProcessorTest {

	static class PlainOfflineWebSettingsTestLoader extends ParametrizedAnnotationConfigContextLoader {
		public PlainOfflineWebSettingsTestLoader() {
			super(PlainParametersTestConfiguration.class);
		}
	}

	@Autowired
	private ParametersManagerFactory parameterManagerFactory;

    @Autowired
    private WebSettingsData webSettingsData;

	@Test
	@DirtiesContext
	public void testOfflineSettings() {
		PlainOfflineWebSettings plainOfflineWebSettings = new PlainOfflineWebSettings(webSettingsData, parameterManagerFactory);
        Assert.assertEquals("There should be COMBINED mode in the params defined", OfflineMode.COMBINED, plainOfflineWebSettings.getOfflineMode());
	}
}
