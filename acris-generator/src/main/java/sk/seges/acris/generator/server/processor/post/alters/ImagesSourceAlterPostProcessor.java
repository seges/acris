package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class ImagesSourceAlterPostProcessor extends AbstractPathAlterPostProcessor {

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {

		if (node instanceof ImageTag) {
			return isPathRelative(getPath(node));
		}

		return false;
	}

	@Override
	protected String getPath(Node node) {
		ImageTag imageTag = (ImageTag) node;
		return imageTag.extractImageLocn();
	}

	@Override
	protected void setPath(Node node, String path) {
		ImageTag imageTag = (ImageTag) node;
		imageTag.setImageURL(path);
	}
}