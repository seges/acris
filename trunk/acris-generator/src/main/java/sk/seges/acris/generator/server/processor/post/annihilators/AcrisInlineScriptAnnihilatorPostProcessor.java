package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.Div;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;


public class AcrisInlineScriptAnnihilatorPostProcessor extends AbstractStyleClassNameAnnihilator {

	public final static String ACRIS_INLINE_SCRIPTS = "acris-inline-scripts";

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
