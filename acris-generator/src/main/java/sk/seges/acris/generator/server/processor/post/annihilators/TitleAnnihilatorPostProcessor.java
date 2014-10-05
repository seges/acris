package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.TitleTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class TitleAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TitleTag) {

			return (generatorEnvironment.getContent() == null ||
					generatorEnvironment.getContent().getTitle() == null || 
					generatorEnvironment.getContent().getTitle().length() == 0);
		}
		
		return false;
	}

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);	
	}
}