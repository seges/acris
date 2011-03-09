package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class LocaleGwtPropertyAppenderPostProcessor extends AbstractElementPostProcessor {

	private static final String NAME_ATTRIBUTE_NAME = "name";
	private static final String CONTENT_ATTRIBUTE_NAME = "content";

	private static final String GWT_PROPERTY_NAME = "gwt:property";
	private static final String LOCALE_PROPERTY_VALUE ="locale=";

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag headTag = (HeadTag)node;
		NodeList headChildNodes = headTag.getChildren();

		MetaTag tag = new MetaTag();
		tag.setAttribute(NAME_ATTRIBUTE_NAME, "\"" + GWT_PROPERTY_NAME + "\"");
		tag.setAttribute(CONTENT_ATTRIBUTE_NAME, LOCALE_PROPERTY_VALUE + generatorEnvironment.getGeneratorToken().getLanguage());

		headChildNodes.add(tag);
		
		return true;
	}
}