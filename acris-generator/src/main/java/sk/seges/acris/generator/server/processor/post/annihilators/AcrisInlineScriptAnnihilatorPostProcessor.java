package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.Div;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;


public class AcrisInlineScriptAnnihilatorPostProcessor extends AbstractStyleClassNameAnnihilator {

	public final static String ACRIS_INLINE_SCRIPTS = "acris-inline-scripts";

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.COMBINED;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected String getStyleClassName() {
		return ACRIS_INLINE_SCRIPTS;
	}
	
	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof Div) {
			return super.supportsNode(node, generatorEnvironment);
		}
		
		return false;
	}
}
