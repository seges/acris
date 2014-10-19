package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.TitleTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class TitleAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TitleTag) {

			return (generatorEnvironment.getContent() == null ||
					generatorEnvironment.getContent().getTitle() == null || 
					generatorEnvironment.getContent().getTitle().length() == 0);
		}
		
		return false;
	}

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);	
	}
}