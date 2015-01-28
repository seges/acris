package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;

public class KeywordsMetaTagAlterPostProcessor extends AbstractMetaTagAlterPostProcessor {

    @Override
	protected String getMetaTagName(GeneratorEnvironment generatorEnvironment) {
		return NodeDefinition.KEYWORDS_TAG_NAME.getName();
	}

	@Override
	protected String getMetaTagContent(GeneratorEnvironment generatorEnvironment) {
		return generatorEnvironment.getContent().getKeywords();
	}
	
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (!super.supports(node, generatorEnvironment)) {
			return false;
		}
		return (generatorEnvironment.getContent() != null && 
				generatorEnvironment.getContent().getKeywords() != null && 
				generatorEnvironment.getContent().getKeywords().length() > 0);
	}
}