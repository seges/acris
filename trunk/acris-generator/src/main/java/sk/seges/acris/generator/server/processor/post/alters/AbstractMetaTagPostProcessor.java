package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.MetaTag;

public abstract class AbstractMetaTagPostProcessor extends AbstractContentInfoPostProcessor {
	
	protected static final String NAME_ATTRIBUTE_NAME = "name";
	protected static final String CONTENT_ATTRIBUTE_NAME = "content";

	protected abstract String getMetaTagName();
	protected abstract String getMetaTagContent();
	
	@Override
	public boolean process(Node node) {
		MetaTag metaTag = (MetaTag)node;
		String content = getMetaTagContent();
		metaTag.setMetaTagContents(content == null ? "" : content);
		return true;
	}

	@Override
	public boolean supports(Node node) {
		if  (!(node instanceof MetaTag)) {
			return false;
		}
		
		MetaTag metaTag = (MetaTag)node;
		String name = metaTag.getAttribute(NAME_ATTRIBUTE_NAME);
		
		if (name == null) {
			return false;
		}
		
		return (name.toLowerCase().equals(getMetaTagName().toLowerCase()));
	}
}