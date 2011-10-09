package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

@Component
public class NiceURLLinkPostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean supports(Node node) {
		if (node instanceof LinkTag) {
			if (((LinkTag)node).getLink().startsWith("#")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean process(Node node) {
		((LinkTag)node).setLink(webSettings.getTopLevelDomain() + "/" + ((LinkTag)node).getLink().substring(1));
		return true;
	}
}