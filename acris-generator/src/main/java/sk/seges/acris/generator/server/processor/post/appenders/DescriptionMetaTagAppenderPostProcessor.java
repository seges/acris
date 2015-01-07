package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;

public class DescriptionMetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

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
	public synchronized boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		if (NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(NodeDefinition.DESCRIPTION_TAG_NAME)) == null) {
			if (generatorEnvironment.getContent() != null && 
				generatorEnvironment.getContent().getDescription() != null && 
				generatorEnvironment.getContent().getDescription().length() > 0) {
					appendMetaTag((HeadTag) node, NodeDefinition.DESCRIPTION_TAG_NAME, generatorEnvironment.getContent().getDescription());
			}
		}
		return true;
	}
}