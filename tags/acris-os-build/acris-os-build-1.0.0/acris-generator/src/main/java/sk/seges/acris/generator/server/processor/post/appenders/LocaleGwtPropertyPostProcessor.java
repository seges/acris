package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

@Component
public class LocaleGwtPropertyPostProcessor extends AbstractElementPostProcessor {

	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String CONTENT_ATTRIBUTE_NAME = "content";

	private static final String GWT_PROPERTY_NAME = "gwt:property";
	private static final String LOCALE_PROPERTY_VALUE ="locale=";

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node) {
		HeadTag headTag = (HeadTag)node;
		NodeList headChildNodes = headTag.getChildren();

		MetaTag tag = new MetaTag();
		tag.setAttribute(NAME_ATTRIBUTE_NAME, "\"" + GWT_PROPERTY_NAME + "\"");
		tag.setAttribute(CONTENT_ATTRIBUTE_NAME, LOCALE_PROPERTY_VALUE + webSettings.getLang());

		headChildNodes.add(tag);
		
		return true;
	}
}