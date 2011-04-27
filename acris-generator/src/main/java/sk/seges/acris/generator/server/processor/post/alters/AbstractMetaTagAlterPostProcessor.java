package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public abstract class AbstractMetaTagAlterPostProcessor extends AbstractAlterPostProcessor {
	
	public static final String NAME_ATTRIBUTE_NAME = "name";

	protected abstract String getMetaTagName(GeneratorEnvironment generatorEnvironment);
	protected abstract String getMetaTagContent(GeneratorEnvironment generatorEnvironment);

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		MetaTag metaTag = (MetaTag)node;
		String metaTagContent = getMetaTagContent(generatorEnvironment);
		metaTag.setMetaTagContents(metaTagContent == null ? "" : metaTagContent);
		return true;
	}

	@Override
	public synchronized boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if  (!(node instanceof MetaTag)) {
			return false;
		}
		
		MetaTag metaTag = (MetaTag)node;
		String name = metaTag.getAttribute(NAME_ATTRIBUTE_NAME);
		
		if (name == null) {
			return false;
		}
		
		return (name.toLowerCase().equals(getMetaTagName(generatorEnvironment).toLowerCase()));
	}
}