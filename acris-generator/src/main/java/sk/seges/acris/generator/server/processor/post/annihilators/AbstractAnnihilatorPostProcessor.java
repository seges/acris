package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractAnnihilatorPostProcessor extends AbstractElementPostProcessor {

	protected abstract boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment);
	protected abstract boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment);

	@Override
	public Kind getKind() {
		return Kind.ANNIHILATOR;
	}
	
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return supportsParent(node, generatorEnvironment);
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {

		NodeList nodeList = node.getChildren();
		
		if (nodeList == null) {
			return false;
		}
		
		int size = nodeList.size();
		
		int removeIndex = -1;
		
		for (int i = 0; i < size; i++) {
		
			Node childNode = nodeList.elementAt(i);
			
			if (supportsNode(childNode, generatorEnvironment)) {
				//amen
				removeIndex = i;
				break;
			}
		}
		
		if (removeIndex == -1) {
			return false;
		}

		nodeList.remove(removeIndex);
		
		return true;
	}
}