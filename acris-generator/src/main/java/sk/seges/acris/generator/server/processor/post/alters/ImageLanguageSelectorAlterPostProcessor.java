package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import sk.seges.acris.generator.server.processor.ContentDataProvider;

import java.util.ArrayList;
import java.util.List;

public class ImageLanguageSelectorAlterPostProcessor extends AbstractLanguageSelectorAlterPostProcessor<ImageTag, ImageTag> {

	public ImageLanguageSelectorAlterPostProcessor(ContentDataProvider contentDataProvider) {
		super(contentDataProvider);
	}

	@Override
	protected Class<ImageTag> getLanguageSelectorTagClass() {
		return ImageTag.class;
	}

	@Override
	protected List<Node> processLocaleTag(String url, String text, ImageTag currentTag, Node parentNode) {
		List<Node> result = new ArrayList<Node>();
		LinkTag anchorNode = createAnchorNode(text, url);
		NodeList children = new NodeList();
		children.add(currentTag);
		anchorNode.setChildren(children);
		result.add(anchorNode);
		return result;
	}

	@Override
	protected String getLanguageName(ImageTag tag) {
		return null;
	}
}