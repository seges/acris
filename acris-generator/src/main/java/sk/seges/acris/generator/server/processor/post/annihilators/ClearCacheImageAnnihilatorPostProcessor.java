package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class ClearCacheImageAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

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
		return true;
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof ImageTag) {
			ImageTag image = (ImageTag)node;
			
			String onload = image.getAttribute("onload");
			String src = image.getAttribute("src");
			
			return (onload != null && onload.length() > 0 && src != null && src.contains("clear.cache.gif"));
		}
		return false;
	}

}
