package sk.seges.acris.generator.server.processor.post.alters;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class SelectLanguageSelectorAlterPostProcessor extends AbstractLanguageSelectorAlterPostProcessor<SelectTag, OptionTag> {

	public SelectLanguageSelectorAlterPostProcessor(ContentDataProvider contentDataProvider) {
		super(contentDataProvider);
	}

	@Override
	protected Class<SelectTag> getLanguageSelectorTagClass() {
		return SelectTag.class;
	}

	@Override
	protected Class<OptionTag> getLanguageSelectorChildTagClass() {
		return OptionTag.class;
	}
	
	@Override
	protected NodeList getLanguageNodes(SelectTag tag, TagNode parentNode) {
		NodeList result = new NodeList();
		for (OptionTag optionTag: tag.getOptionTags()) {
			result.add(optionTag);
		}
		return result;
	}
	
	@Override
	protected boolean processTag(SelectTag tag, TagNode parentNode, GeneratorEnvironment generatorEnvironment) {

		String style = parentNode.getAttribute(STYLE_ATTRIBUTE_NAME);
		
		if (style == null || style.length() == 0) {
			style = LANGUAGE_BAR_STYLE;
		} else {
			style = style + ";" + LANGUAGE_BAR_STYLE;
		}
		
		parentNode.setAttribute(STYLE_ATTRIBUTE_NAME, style);

		return super.processTag(tag, parentNode, generatorEnvironment);
	}
	
	@Override
	protected List<Node> processLocaleTag(String url, String text, OptionTag currentTag, Node parentNode) {
		List<Node> result = new ArrayList<Node>();
		LinkTag anchorNode = createAnchorNode(text, url);
		result.add(new TextNode("\t\t"));
		result.add(anchorNode);
		result.add(new TextNode("<br/>"));
		return result;
	}

	protected String getLanguageShort(OptionTag tag) {
		return tag.getValue();
	}

	@Override
	protected String getLanguageName(OptionTag tag) {
		return tag.getOptionText();
	}	

}
