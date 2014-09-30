package sk.seges.acris.generator.server.processor.post.alters;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.htmltags.LenientLinkTag;

public class LinkLanguageSelectorAlterPostProcessor extends AbstractLanguageSelectorAlterPostProcessor<LenientLinkTag, LenientLinkTag> {

	public LinkLanguageSelectorAlterPostProcessor(ContentDataProvider contentDataProvider) {
		super(contentDataProvider);
	}

	@Override
	protected Class<LenientLinkTag> getLanguageSelectorTagClass() {
		return LenientLinkTag.class;
	}

	@Override
	protected List<Node> processLocaleTag(String url, String text, LenientLinkTag currentTag, Node parentNode) {
		List<Node> result = new ArrayList<Node>();
		currentTag.setLink(url);
		result.add(currentTag);
		return result;
	}

	@Override
	protected String getLanguageName(LenientLinkTag tag) {
		return tag.getLinkText();
	}
}
