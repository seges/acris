package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import org.htmlparser.tags.FrameTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.CSSStyleDetector;

public class NotVisibleTagsAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	@Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return node != null && node.getChildren() != null;
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TagNode && !(node instanceof FrameTag)) {
			return !(new CSSStyleDetector((Tag)node).isVisible());
		}
		
		return false;
	}
}