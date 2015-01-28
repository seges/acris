package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.FrameTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.CSSStyleDetector;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class NotVisibleTagsAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	@Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
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