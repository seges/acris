package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class NiceURLLinkPostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof LinkTag) {
			return (((LinkTag)node).getLink().startsWith("#"));
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getWebSettings().getTopLevelDomain() == null) {
			((LinkTag)node).setLink("/" + getLink(((LinkTag)node).getLink(), generatorEnvironment));
		} else {
			String url = generatorEnvironment.getWebSettings().getTopLevelDomain();
			if (!url.endsWith("/")) {
				url += "/";
			}
			((LinkTag)node).setLink(url + getLink(((LinkTag)node).getLink(), generatorEnvironment));
		}
		return true;
	}
	
	protected String getLink(String link, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getGeneratorToken().isDefaultToken()) {
			return "";
		}
		return link.substring(1);
	}
}