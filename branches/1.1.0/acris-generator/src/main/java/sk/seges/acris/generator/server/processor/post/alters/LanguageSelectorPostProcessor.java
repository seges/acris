package sk.seges.acris.generator.server.processor.post.alters;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class LanguageSelectorPostProcessor extends AbstractElementPostProcessor {

	private String LANGUAGE_SELECTOR_STYLE_CLASS_NAME = "acris-language-selector-panel";
	private String CLASS_ATTRIBUTE_NAME = "class";
	private String STYLE_ATTRIBUTE_NAME = "style";
		
	private String LANGUAGE_BAR_STYLE = "height: 32px; overflow-y: scroll";
	
    private static final Logger logger = Logger.getLogger(LanguageSelectorPostProcessor.class);
    
    private ContentDataProvider contentDataProvider;
    
    public LanguageSelectorPostProcessor(ContentDataProvider contentDataProvider) {
    	this.contentDataProvider = contentDataProvider;
    }
    
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
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
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		
		SelectTag languageSelector = interateToNode(node, SelectTag.class);
		
		if (languageSelector == null) {
			logger.error("Language selector does not contains any options. Original HTML is " + node.toHtml());
			return false;
		}
		
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
			
			if (languageValue == generatorEnvironment.getGeneratorToken().getLanguage()) {
				continue;
			}
			
			WebSettingsData linkWebSettings = generatorEnvironment.getWebSettings();

			LinkTag linkTag = new LinkTag();
			
			TextNode textNode = new TextNode(languageText);
			
			NodeList nodeList = new NodeList();
			nodeList.add(textNode);
			linkTag.setChildren(nodeList);
			
			if (generatorEnvironment.getContent() == null) {
				continue;
			}
			
			ContentData<?> content = contentDataProvider.getContentForLanguage(generatorEnvironment.getContent(), languageValue);
			
			if (content == null) {
				continue;
			}
			
			String translatedNiceUrl = content.getNiceUrl();
			String url = linkWebSettings.getTopLevelDomain();
			
			if (url == null) {
				logger.warn("Web " + generatorEnvironment.getGeneratorToken().getWebId() + " doesn't have set top level domain. Using empty URL!");
				url = "";
			}
			
			if (translatedNiceUrl != null) {
				if (!url.endsWith("/")) {
					url += "/";
				}
				url += translatedNiceUrl;
			}
			linkTag.setLink(url);
			languageLinksList.add(new TextNode("\t\t"));
			languageLinksList.add(linkTag);
			languageLinksList.add(new TextNode("</a>"));
			languageLinksList.add(new TextNode("<br/>"));
		}
		
		languageLinksList.add(new TextNode("\t"));

		node.setChildren(languageLinksList);
		
		return true;
	}
}