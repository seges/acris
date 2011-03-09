package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;

public class MetaTagPostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getWebSettings().getMetaData() == null) {
			return true;
		}

		for (MetaData metaData : generatorEnvironment.getWebSettings().getMetaData()) {
			MetaTag metaTag = NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(metaData.getType().getName()));
			if (metaTag != null) {
				String content = metaData.getContent();
				metaTag.setMetaTagContents(content == null ? "" : content);
			}
		}

		return false;
	}

}
