package sk.seges.acris.generator.server.manager;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.manager.JSONOfflineWebSettingsTest.JSONOfflineWebSettingsTestLoader;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.JSONTestConfiguration;
import sk.seges.acris.site.server.model.data.WebSettingsData;
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

    @Autowired
    private WebSettingsData webSettingsData;

    @Test
	@DirtiesContext
	public void testOfflineSettings() {
        parameterManagerFactory.create(webSettingsData.getParameters());
        JSONOfflineWebSettings offlineWebSettings = new JSONOfflineWebSettings(webSettingsData, parameterManagerFactory);
        Assert.assertEquals("There should be COMBINED mode in the params defined", OfflineClientWebParams.OfflineMode.COMBINED, offlineWebSettings.getOfflineMode());
	}
}
