package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;

@Component
public class StylesPathPostProcessor extends AbstractPathPostProcessor {

	private static final String STYLESHEET_REL = "stylesheet";
	private static final String CSS_TYPE = "text/css";
	
	@Override
	public boolean supports(Node node) {
		
		if (node instanceof StyleLinkTag) {
			StyleLinkTag styleLinkTag = (StyleLinkTag)node;
			return (isPathRelative(getPath(node)) &&	
					compareIgnoreCaseNullSafe(styleLinkTag.getRel(), STYLESHEET_REL) &&
					(compareIgnoreCaseNullSafe(styleLinkTag.getType(), CSS_TYPE) ||
					 compareIgnoreCaseNullSafe(styleLinkTag.getType(), null)));
		}

		return false;
	}

	@Override
	protected String getPath(Node node) {
		StyleLinkTag styleLinkTag = (StyleLinkTag)node;
		return styleLinkTag.getHref();
	}

	@Override
	protected void setPath(Node node, String path) {
		StyleLinkTag styleLinkTag = (StyleLinkTag)node;
		styleLinkTag.setHref(path);
	}
}