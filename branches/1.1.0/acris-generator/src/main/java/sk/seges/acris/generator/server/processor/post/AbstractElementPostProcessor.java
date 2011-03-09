package sk.seges.acris.generator.server.processor.post;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public abstract class AbstractElementPostProcessor {

	public abstract boolean supports(Node node, GeneratorEnvironment generatorEnvironment);

	public abstract boolean process(Node node, GeneratorEnvironment generatorEnvironment);

	protected AbstractElementPostProcessor() {
	}
	
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
}