package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.ContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractContentInfoPostProcessor extends AbstractElementPostProcessor {

	private ContentInfoProvider contentInfoProvider;
	
	@Override
	public boolean process(Node node) {
		return false;
	}

	@Override
	public boolean supports(Node node) {
		return false;
	}
}
