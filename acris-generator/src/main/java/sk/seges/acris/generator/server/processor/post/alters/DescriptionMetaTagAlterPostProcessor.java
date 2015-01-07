package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.TokenSupport;


public class DescriptionMetaTagAlterPostProcessor extends AbstractMetaTagAlterPostProcessor {

	@Override
	protected String getMetaTagName(GeneratorEnvironment generatorEnvironment) {
		return NodeDefinition.DESCRIPTION_TAG_NAME.getName();
	}

	@Override
	protected String getMetaTagContent(GeneratorEnvironment generatorEnvironment) {
		return generatorEnvironment.getContent().getDescription();
	}

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (!super.supports(node, generatorEnvironment)) {
			return false;
		}
		return (generatorEnvironment.getContent() != null &&
				generatorEnvironment.getContent().getDescription() != null && 
				generatorEnvironment.getContent().getDescription().length() > 0);
	}

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }
}