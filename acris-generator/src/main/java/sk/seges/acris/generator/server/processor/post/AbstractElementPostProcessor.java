package sk.seges.acris.generator.server.processor.post;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public abstract class AbstractElementPostProcessor implements PostProcessorKind {

	protected static final String CLASS_ATTRIBUTE_NAME = "class";

	public abstract boolean supports(Node node, GeneratorEnvironment generatorEnvironment);

	public abstract boolean process(Node node, GeneratorEnvironment generatorEnvironment);

	protected AbstractElementPostProcessor() {
	}

    public abstract OfflineClientWebParams.OfflineMode getOfflineMode();

    public abstract TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode);

	protected <T> boolean hasChildNode(Node node, Class<T> nodeClass) {
		return iterateToNode(node, nodeClass) != null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T iterateToNode(Node node, Class<T> nodeClass) {
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

			T result = iterateToNode(childNode, nodeClass);

			if (result != null) {
				return result;
			}
		}

		return null;
	}
}