package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.factory.NodeFactory;
import sk.seges.acris.generator.server.processor.post.alters.AbstractContentInfoPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class TitlePostProcessor extends AbstractContentInfoPostProcessor {

	public TitlePostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean process(Node node) {
		HeadTag headTag = (HeadTag) node;

		TitleTag titleTag = NodesUtils.setTitle(NodeFactory.getTagWithClosing(TitleTag.class), contentInfoProvider.getContentTitle(generatorToken));

		if (headTag.getChildren() == null) {
			headTag.setChildren(new NodeList());
		}

		headTag.getChildren().add(titleTag);

		return true;
	}

	@Override
	public boolean supports(Node node) {
		if (!(node instanceof HeadTag)) {
			return false;
		}

		return (NodesUtils.getChildNode(node, TitleTag.class) == null);
	}
}