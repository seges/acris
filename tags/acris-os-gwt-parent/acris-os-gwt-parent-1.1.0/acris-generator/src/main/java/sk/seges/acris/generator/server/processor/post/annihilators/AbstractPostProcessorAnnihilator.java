package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractPostProcessorAnnihilator extends AbstractElementPostProcessor {

	protected AbstractPostProcessorAnnihilator(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	protected abstract boolean supportsParent(Node node);
	protected abstract boolean supportsNode(Node node);
		
	@Override
	public boolean supports(Node node) {
		return supportsParent(node);
	}

	@Override
	public boolean process(Node node) {

		NodeList nodeList = node.getChildren();
		
		if (nodeList == null) {
			return false;
		}
		
		int size = nodeList.size();
		
		int removeIndex = -1;
		
		for (int i = 0; i < size; i++) {
		
			Node childNode = nodeList.elementAt(i);
			
			if (supportsNode(childNode)) {
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
