package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public abstract class AbstractMetaTagContentAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	protected abstract String getMetaTagContent();

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof MetaTag) {
			MetaTag metaTag = (MetaTag) node;
			String metaTagContent = metaTag.getMetaContent();
			if (metaTagContent != null) {
				metaTagContent = metaTagContent.toLowerCase();
			}
			return getMetaTagContent().toLowerCase().equals(metaTagContent);
		}

		return false;
	}
}