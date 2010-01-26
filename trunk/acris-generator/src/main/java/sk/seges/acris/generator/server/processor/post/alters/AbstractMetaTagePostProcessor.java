package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.springframework.beans.factory.annotation.Autowired;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractMetaTagePostProcessor extends AbstractElementPostProcessor {

	@Autowired
//	private I
	
	@Override
	public boolean process(Node node) {
		return false;
	}

	@Override
	public boolean supports(Node node) {
		return false;
	}
}
