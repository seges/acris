package sk.seges.acris.generator.server.processor.model.api;

import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment.NodesContext;

public class DefaultNodesContext implements NodesContext {

	private HeadTag headNode;
	private Html htmlNode;
	
	public void setHeadNode(HeadTag headNode) {
		this.headNode = headNode;
	}
	
	public HeadTag getHeadNode() {
		return headNode;
	}

	@Override
	public Html getHtmlNode() {
		return htmlNode;
	}

	@Override
	public void setHtmlNode(Html htmlNode) {
		this.htmlNode = htmlNode;
	}
}