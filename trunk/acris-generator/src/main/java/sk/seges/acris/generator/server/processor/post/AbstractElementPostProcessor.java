package sk.seges.acris.generator.server.processor.post;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;

import sk.seges.acris.generator.server.WebSettings;
import sk.seges.acris.generator.server.service.IWebSettingsService;

public abstract class AbstractElementPostProcessor {
	
	@Autowired
	protected IWebSettingsService webSettingsService;

	public abstract boolean supports(Node node);

	public abstract boolean replace(Node node);
	
	protected WebSettings webSettings;

	@SuppressWarnings("unchecked")
	protected <T> T interateToNode(Node node, Class<T> nodeClass) {
		NodeList nodeList = node.getChildren();
		int size = nodeList.size();
		
		for (int i = 0; i < size; i++) {
		
			Node childNode = nodeList.elementAt(i);

			if (childNode.getClass().equals(nodeClass)) {
				return (T)childNode;
			}
			
			T result = interateToNode(childNode, nodeClass);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}

	public void setPostProcessorPageId(String webId, String lang) {
		webSettings = webSettingsService.getWebSettings(webId, lang);
	}
}