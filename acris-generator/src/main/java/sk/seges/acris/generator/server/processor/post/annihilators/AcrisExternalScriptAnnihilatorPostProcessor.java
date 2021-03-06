package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.post.appenders.AcrisExternalScriptAppenderPostProcessor;
import sk.seges.acris.generator.server.processor.utils.CSSStyleClassDetector;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class AcrisExternalScriptAnnihilatorPostProcessor extends AbstractElementPostProcessor {

	@Override
	public Kind getKind() {
		return Kind.ANNIHILATOR;
	}

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.OFFLINE;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TagNode) {
			return new CSSStyleClassDetector((TagNode)node).hasStyleClass(AcrisExternalScriptAppenderPostProcessor.EXTERNAL_SCRIPTS_CLASS_NAME);
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		node.getParent().getChildren().remove(node);
		return true;
	}
}
