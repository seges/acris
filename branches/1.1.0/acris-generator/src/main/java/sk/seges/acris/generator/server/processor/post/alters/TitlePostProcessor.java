package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.TitleTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;

/**
 * Post processor modifies title in the header regarding the content
 * 
 * @author Peter Simun (simun@seges.sk)
 */
public class TitlePostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		NodesUtils.setTitle((TitleTag) node, generatorEnvironment.getContent().getTitle());
		return true;
	}

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof TitleTag);
	}
}