package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.factory.NodeFactory;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class OfflineTagAppenderPostProcessor extends AbstractAppenderPostProcessor {

	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String TYPE_ATTRIBUTE_JS_VALUE = "text/javascript";

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
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag headTag = (HeadTag)node;
		NodeList headChildNodes = headTag.getChildren();

		ScriptTag tag = new ScriptTag();
		tag.setAttribute(TYPE_ATTRIBUTE_NAME, TYPE_ATTRIBUTE_JS_VALUE);
		NodesUtils.setText(tag, "var offline = true;");
		if (headTag.getChildren() == null) {
			headTag.setChildren(new NodeList());
		}

		NodeFactory.encloseTag(tag);
		
		headChildNodes.add(tag);
		
		return true;
	}

}
