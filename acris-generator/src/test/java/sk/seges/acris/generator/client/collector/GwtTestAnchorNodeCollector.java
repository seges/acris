package sk.seges.acris.generator.client.collector;

import com.google.gwt.user.client.ui.RootPanel;
import junit.framework.Assert;
import sk.seges.acris.generator.client.collector.panel.AnchorPanel;
import sk.seges.acris.generator.client.context.DefaultGeneratorClientEnvironment;
import sk.seges.acris.generator.client.context.MapTokenCache;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import java.util.HashSet;
import java.util.Set;

public class GwtTestAnchorNodeCollector {

	private GeneratorClientEnvironment generatorEnvironment;
	
//	@Override
	protected void gwtSetUp() throws Exception {
		
		RootPanel.get().add(new AnchorPanel());

		generatorEnvironment = new DefaultGeneratorClientEnvironment(new MapTokenCache());
		GeneratorToken generatorToken = new GeneratorToken();
		generatorToken.setDefaultToken(true);
		generatorToken.setLanguage("en");
		generatorToken.setNiceUrl("test");
		generatorToken.setWebId("www.seges.sk");

		generatorEnvironment.getTokensCache().setDefaultToken(generatorToken);
		
		Set<String> tokens = new HashSet<String>();
		tokens.add("test");
		generatorEnvironment.getTokensCache().addTokens(tokens);
		
		generatorEnvironment.setServerURL("");
		generatorEnvironment.setTopLevelDomain("http://www.seges.sk");
	}
	
	public void testCollecting() {
		
		generatorEnvironment.getTokensCache().next();
		
		AnchorNodeCollector anchorNodeCollector = new AnchorNodeCollector();
		anchorNodeCollector.collect(RootPanel.get().getElement(), generatorEnvironment);
		
		Assert.assertEquals("There were incorrect number of the collected tokens.", 5, generatorEnvironment.getTokensCache().getWaitingTokensCount());

		while (generatorEnvironment.getTokensCache().hasNext()) {
			String niceUrl = generatorEnvironment.getTokensCache().next().getNiceUrl();
			
			boolean found = false;
			
			for (int i = 1; i < 6; i++) {
				if (("token" + i).equals(niceUrl)) {
					found = true;
					break;
				}
				if (!found) {
					Assert.assertFalse("Token " + niceUrl + " was not supposed to be collected", false);
				}
			}
		}
	}

//	@Override
	public String getName() {
		return "testCollecting";
	}
	
//	@Override
	public String getModuleName() {
		return "sk.seges.acris.generator.AnchorTest";
	}
}