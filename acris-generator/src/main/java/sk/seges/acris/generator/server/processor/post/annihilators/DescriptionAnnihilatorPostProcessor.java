package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class DescriptionAnnihilatorPostProcessor extends AbstractMetaTagNameAnnihilatorPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (!super.supportsNode(node, generatorEnvironment)) {
			return false;
		}

		return (generatorEnvironment.getContent() == null ||
				generatorEnvironment.getContent().getDescription() == null || 
				generatorEnvironment.getContent().getDescription().length() == 0);
	}

	@Override
	protected String getMetaTagName() {
		return NodeDefinition.DESCRIPTION_TAG_NAME.getName();
	}
}