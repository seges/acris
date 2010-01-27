package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.TitleTag;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class TitlePostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean process(Node node) {
		TitleTag titleTag = (TitleTag)node;
//		titleTag.setText();
		return false;
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof TitleTag);
	}

}
