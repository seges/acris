package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.factory.NodeFactory;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;

public class TitleAppenderPostProcessor extends AbstractAppenderPostProcessor {

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag headTag = (HeadTag) node;

		if (generatorEnvironment.getContent() != null && 
			generatorEnvironment.getContent().getTitle() != null && 
			generatorEnvironment.getContent().getTitle().length() > 0) {

			TitleTag titleTag = NodesUtils.setText(NodeFactory.getTagWithClosing(TitleTag.class), generatorEnvironment.getContent().getTitle());
	
			if (headTag.getChildren() == null) {
				headTag.setChildren(new NodeList());
			}
	
			headTag.getChildren().add(titleTag);
		}
		
		return true;
	}

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (!(node instanceof HeadTag)) {
			return false;
		}

		if (generatorEnvironment.getContent() == null || generatorEnvironment.getContent().getTitle() == null) {
			return false;
		}
		
		return (NodesUtils.getChildNode(node, TitleTag.class) == null);
	}
}