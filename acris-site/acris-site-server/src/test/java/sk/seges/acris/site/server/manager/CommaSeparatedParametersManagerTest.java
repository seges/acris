package sk.seges.acris.site.server.manager;

import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

import sk.seges.acris.site.server.manager.CommaSeparatedParametersManager;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public class CommaSeparatedParametersManagerTest extends TestCase {

	@Test
	public void testParameters() {
		String parameters = "offline.post.processor.inactive=;offline.index.post.processor.inactive=NocacheScriptPostProcessor,PropertiesScriptPostProcessor";
		CommaSeparatedParametersManager commaSeparatedParametersManager = new CommaSeparatedParametersManager(parameters);
		assertEquals("There should be 2 parameters in the result.", 2, commaSeparatedParametersManager.getParameters().size());
		Iterator<? extends ParameterData> iterator = commaSeparatedParametersManager.getParameters().iterator();
		ParameterData inactive = iterator.next();
		assertEquals("Wrong parameter key", "offline.post.processor.inactive", inactive.getKey());
		assertEquals("Wrong parameter value", commaSeparatedParametersManager.getParameterValue(inactive), "");

		ParameterData inactiveIndex = iterator.next();
		assertEquals("Wrong parameter key", "offline.index.post.processor.inactive", inactiveIndex.getKey());
		assertEquals("Wrong parameter value", commaSeparatedParametersManager.getParameterValue(inactiveIndex), "NocacheScriptPostProcessor,PropertiesScriptPostProcessor");
	}
}
