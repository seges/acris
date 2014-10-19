package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;

public class LocaleGwtPropertyAlterPostProcessor extends AbstractAlterPostProcessor {

	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String CONTENT_ATTRIBUTE_NAME = "content";

	private static final String GWT_PROPERTY_NAME = "gwt:property";
	private static final String LOCALE_PROPERTY_VALUE ="locale=";

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		MetaTag gwtPropertyTag = NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(GWT_PROPERTY_NAME));
		if (gwtPropertyTag == null) {

			HeadTag headTag = (HeadTag)node;
			NodeList headChildNodes = headTag.getChildren();

			boolean newList = false;
			
			if (headChildNodes == null) {
				headChildNodes = new NodeList();
				newList = true;
			}
			
			MetaTag tag = new MetaTag();
			tag.setEmptyXmlTag(true);
			
			tag.setAttribute(NAME_ATTRIBUTE_NAME, "\"" + GWT_PROPERTY_NAME + "\"");
			tag.setAttribute(CONTENT_ATTRIBUTE_NAME, LOCALE_PROPERTY_VALUE + generatorEnvironment.getGeneratorToken().getLanguage());
			
			headChildNodes.add(tag);

			if (newList) {
				headTag.setChildren(headChildNodes);
			}
		} else {
			gwtPropertyTag.setAttribute(CONTENT_ATTRIBUTE_NAME, LOCALE_PROPERTY_VALUE + generatorEnvironment.getGeneratorToken().getLanguage());
		}
		
		return true;
	}
}