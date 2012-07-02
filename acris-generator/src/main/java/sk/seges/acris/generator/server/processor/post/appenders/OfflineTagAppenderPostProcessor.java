package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class OfflineTagAppenderPostProcessor extends AbstractAppenderPostProcessor {

	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String TYPE_ATTRIBUTE_JS_VALUE = "text/javascript";

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag headTag = (HeadTag)node;
		NodeList headChildNodes = headTag.getChildren();

		ScriptTag tag = new ScriptTag();
		tag.setAttribute(TYPE_ATTRIBUTE_NAME, TYPE_ATTRIBUTE_JS_VALUE);
		tag.setText("var offline = true;");
		
		headChildNodes.add(tag);
		
		return true;
	}

}
