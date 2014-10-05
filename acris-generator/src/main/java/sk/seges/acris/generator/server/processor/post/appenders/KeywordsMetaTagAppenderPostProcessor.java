package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;

public class KeywordsMetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		if (NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(NodeDefinition.KEYWORDS_TAG_NAME)) == null) {
			if (generatorEnvironment.getContent() != null && 
				generatorEnvironment.getContent().getKeywords() != null && 
				generatorEnvironment.getContent().getKeywords().length() > 0) {
					appendMetaTag((HeadTag) node, NodeDefinition.KEYWORDS_TAG_NAME, generatorEnvironment.getContent().getKeywords());
			}
		}
		return true;
	}
}