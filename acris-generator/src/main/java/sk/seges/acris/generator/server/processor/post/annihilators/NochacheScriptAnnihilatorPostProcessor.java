package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class NochacheScriptAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        if (offlineMode.contains(OfflineClientWebParams.OfflineMode.COMBINED)) {
            return TokenSupport.NON_DEFAULT;
        }
        return TokenSupport.ALL;
    }

    @Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);	
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof ScriptTag) {
			String jsSource = ((ScriptTag)node).getAttribute("src");
			return (jsSource != null && jsSource.toLowerCase().indexOf(".nocache.js") > 0); 
		}
		
		return false;
	}
}