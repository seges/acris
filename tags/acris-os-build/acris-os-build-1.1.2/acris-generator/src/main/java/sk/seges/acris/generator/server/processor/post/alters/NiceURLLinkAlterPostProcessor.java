package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.AnchorUtils;

public class NiceURLLinkAlterPostProcessor extends AbstractAlterPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof LinkTag) {
			return (((LinkTag)node).getLink().startsWith("#"));
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		LinkTag linkNode = (LinkTag)node;
		linkNode.setLink(AnchorUtils.getAnchorTargetHref(getLink(linkNode.getLink(), generatorEnvironment), generatorEnvironment));
		
		return true;
	}

	protected String getLink(String link, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getGeneratorToken().isDefaultToken()) {
			return "";
		}
		return link.substring(1);
	}
}