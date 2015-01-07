package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class GoogleAnalyticAppenderPostProcessor extends AbstractAppenderPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag head = (HeadTag)node;
		if (!validateAnalyticsData(generatorEnvironment.getWebSettings().getAnalyticsScriptData())) {
			return false;
		}

		TextNode textTag = new TextNode(generatorEnvironment.getWebSettings().getAnalyticsScriptData());
		head.getChildren().add(textTag);
		
		return true;
	}
	
	private boolean validateAnalyticsData(String analyticsScript) {
		return (analyticsScript != null && analyticsScript.length() > 0);
	}
}