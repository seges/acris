package sk.seges.acris.generator.server.processor.post;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractElementPostProcessor {

	@Autowired
	protected IWebSettingsService webSettingsService;

	public abstract boolean supports(Node node);

	public abstract boolean process(Node node);

	protected WebSettingsData webSettings;
	protected GeneratorToken generatorToken;

	@SuppressWarnings("unchecked")
	protected <T> T interateToNode(Node node, Class<T> nodeClass) {
		NodeList nodeList = node.getChildren();

		if (nodeList == null) {
			//Node does not have any child
			return null;
		}

		int size = nodeList.size();

		for (int i = 0; i < size; i++) {

			Node childNode = nodeList.elementAt(i);

			if (childNode.getClass().equals(nodeClass)) {
				return (T) childNode;
			}

			T result = interateToNode(childNode, nodeClass);

			if (result != null) {
				return result;
			}
		}

		return null;
	}

	public void setPostProcessorPageId(GeneratorToken token) {
		this.generatorToken = token;
		webSettings = webSettingsService.getWebSettings(token.getWebId());
	}
}