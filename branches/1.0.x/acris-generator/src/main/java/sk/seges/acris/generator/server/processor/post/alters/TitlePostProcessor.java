package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TitleTag;
import org.springframework.stereotype.Component;

@Component
public class TitlePostProcessor extends AbstractContentInfoPostProcessor {

	@Override
	public boolean process(Node node) {
		TitleTag titleTag = (TitleTag)node;
		String title = contentInfoProvider.getContentTitle(generatorToken);
		if (title == null) {
			title = "";
		}
		TextNode titleText = null;
		if (titleTag.getChildCount() == 0) {
			titleText = new TextNode(title);
			titleTag.getChildren().add(titleText);
		} else {
			titleTag.getChildren().elementAt(0).setText(title);
		}
		
		return true;
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof TitleTag);
	}

}
