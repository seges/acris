package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.WebSettings;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

@Component
public class LanguageSelectorPostProcessor extends AbstractElementPostProcessor {

	private String LANGUAGE_SELECTOR_STYLE_CLASS_NAME = "acris-language-selector-panel";
	private String CLASS_ATTRIBUTE_NAME = "class";
	private String STYLE_ATTRIBUTE_NAME = "style";
		
	private String LANGUAGE_BAR_STYLE = "height: 32px; overflow-y: scroll";
	
	@Override
	public boolean supports(Node node) {
		if (node instanceof TagNode) {
			String classAttributeValue = ((TagNode)node).getAttribute(CLASS_ATTRIBUTE_NAME);
			
			if (classAttributeValue != null && classAttributeValue.length() > 0) {
				String[] styleClasses = classAttributeValue.split(" ");
				
				for (String styleClass : styleClasses) {
					if (styleClass.trim().equals(LANGUAGE_SELECTOR_STYLE_CLASS_NAME)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean process(Node node) {
		
		SelectTag languageSelector = interateToNode(node, SelectTag.class);

		String style = ((TagNode)node).getAttribute(STYLE_ATTRIBUTE_NAME);
		
		if (style == null || style.length() == 0) {
			style = LANGUAGE_BAR_STYLE;
		} else {
			style = style + ";" + LANGUAGE_BAR_STYLE;
		}
		
		((TagNode)node).setAttribute(STYLE_ATTRIBUTE_NAME, style);
		
		NodeList languageLinksList = new NodeList();
		
		for (OptionTag optionTag : languageSelector.getOptionTags()) {
			String languageText = optionTag.getOptionText(); //English
			String languageValue = optionTag.getValue(); //en
			
			WebSettings linkWebSettings = webSettingsService.getWebSettings(webSettings.getWebId(), languageValue);

			LinkTag linkTag = new LinkTag();
			
			TextNode textNode = new TextNode(languageText);
			
			NodeList nodeList = new NodeList();
			nodeList.add(textNode);
			linkTag.setChildren(nodeList);
			
			linkTag.setLink(linkWebSettings.getTopLevelDomain());
			
			languageLinksList.add(linkTag);
			languageLinksList.add(new TextNode("<br/>"));
		}
		
		node.setChildren(languageLinksList);
		
		return true;
	}
}