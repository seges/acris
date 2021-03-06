package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

/**
 * Relativize path in the stylesheet
 * 
 * @author psimun
 */
public class StylesAlterPathPostProcessor extends AbstractPathAlterPostProcessor {

	private static final String STYLESHEET_REL = "stylesheet";
	private static final String CSS_TYPE = "text/css";

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

		if (node instanceof StyleLinkTag) {
			StyleLinkTag styleLinkTag = (StyleLinkTag) node;
			return (isPathRelative(getPath(node)) && compareIgnoreCaseNullSafe(styleLinkTag.getRel(), STYLESHEET_REL) && (compareIgnoreCaseNullSafe(
					styleLinkTag.getType(), CSS_TYPE) || compareIgnoreCaseNullSafe(styleLinkTag.getType(), null)));
		}

		return false;
	}

	@Override
	protected String getPath(Node node) {
		StyleLinkTag styleLinkTag = (StyleLinkTag) node;
		return styleLinkTag.getHref();
	}

	@Override
	protected void setPath(Node node, String path) {
		StyleLinkTag styleLinkTag = (StyleLinkTag) node;
		styleLinkTag.setHref(path);
	}
}