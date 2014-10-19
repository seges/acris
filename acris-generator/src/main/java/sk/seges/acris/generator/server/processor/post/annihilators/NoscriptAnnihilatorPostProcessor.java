package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.BodyTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.htmltags.NoScriptTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class NoscriptAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.OFFLINE;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof BodyTag);	
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof NoScriptTag);
	}

}