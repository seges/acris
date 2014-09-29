package sk.seges.acris.generator.server.processor.post.alters;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.htmltags.LenientLinkTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public class LanguageSelectorAlterPostProcessor extends AbstractAlterPostProcessor {

	private static final String LANGUAGE_SELECTOR_STYLE_CLASS_NAME = "acris-language-selector-panel";
	private static final String STYLE_ATTRIBUTE_NAME = "style";
		
	private String LANGUAGE_BAR_STYLE = "height: 32px; overflow-y: scroll";
	
    private static final Logger logger = Logger.getLogger(LanguageSelectorAlterPostProcessor.class);
    
    private ContentDataProvider contentDataProvider;
    
    public LanguageSelectorAlterPostProcessor(ContentDataProvider contentDataProvider) {
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
		
		Tag languageSelectorTag = interateToNode(node, SelectTag.class);
		Tag languageSelectorLinkTag = interateToNode(node, LenientLinkTag.class);
		Tag languageSelectorImageTag = interateToNode(node, ImageTag.class);
		
		if (languageSelectorTag == null && languageSelectorLinkTag == null && languageSelectorImageTag == null) {			
			logger.error("Language selector does not contains any options. Original HTML is " + node.toHtml());
			return false;						
		}		
		
		NodeList languageLinksList = new NodeList();
		
		if (languageSelectorTag != null){
			String style = ((TagNode)node).getAttribute(STYLE_ATTRIBUTE_NAME);
			
			if (style == null || style.length() == 0) {
				style = LANGUAGE_BAR_STYLE;
			} else {
				style = style + ";" + LANGUAGE_BAR_STYLE;
			}
			
			((TagNode)node).setAttribute(STYLE_ATTRIBUTE_NAME, style);
			
			for (OptionTag optionTag : ((SelectTag)languageSelectorTag).getOptionTags()) {
				String languageText = optionTag.getOptionText(); //English
				String languageValue = optionTag.getValue(); //en
				
				createLinkOption(generatorEnvironment, languageValue, languageText, languageLinksList, null, "<br/>");
			}
			
			languageLinksList.add(new TextNode("\t"));

			node.setChildren(languageLinksList);
		} else if(languageSelectorLinkTag != null){
			for (int i = 1; i < node.getChildren().size(); i++){
				Node childNode = node.getChildren().elementAt(i);
				if(childNode.getClass().equals(LenientLinkTag.class)){
					LenientLinkTag lenientLinkTag = (LenientLinkTag)childNode;
					String languageText = lenientLinkTag.getLinkText();
					String languageValue = lenientLinkTag.getAttribute("locale");
					
					createLinkOption(generatorEnvironment, languageValue, languageText, languageLinksList, lenientLinkTag, null);
				}								
			}
		} else if(languageSelectorImageTag != null){
			for (int i = 1; i < node.getChildren().size(); i++){
				Node childNode = node.getChildren().elementAt(i);
				if(childNode.getClass().equals(ImageTag.class)){
					ImageTag imageTag = (ImageTag)childNode;
					String languageText = "<" + imageTag.getText() + ">";// image as textNode
					String languageValue = imageTag.getAttribute("locale");
					
					createLinkOption(generatorEnvironment, languageValue, languageText, languageLinksList, null, "");
				}								
			}
			languageLinksList.add(new TextNode("\t"));

			node.setChildren(languageLinksList);
		}
		
		return true;
	}
	
	private void createLinkOption(GeneratorEnvironment generatorEnvironment, String languageValue, String languageText, 
			NodeList languageLinksList, LinkTag existingLinkTag, String linkSeparator){
		if (languageValue == generatorEnvironment.getGeneratorToken().getLanguage()) {
			return;
		}
		
		WebSettingsData linkWebSettings = generatorEnvironment.getWebSettings();
		
		if (generatorEnvironment.getContent() == null) {
			return;
		}
		logger.debug("creating link for language: " + languageValue + " and content (generatorEnvironment.getContent()) with nice url: " + generatorEnvironment.getContent().getNiceUrl());
		ContentData content = contentDataProvider.getContentForLanguage(generatorEnvironment.getContent(), languageValue);
		
		if (content == null) {			
			content = contentDataProvider.getHomeContent(languageValue, generatorEnvironment.getContent().getWebId());
			if(content == null){
				logger.error("No home page found for webId: " + generatorEnvironment.getContent().getWebId() + " and language: " + languageValue);
				return;
			}
		}
		logger.debug("found content for language: " + languageValue + " with nice url: " + content.getNiceUrl());
		
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
		
		if(existingLinkTag == null){
			LinkTag linkTag = new LinkTag();
			
			TextNode textNode = new TextNode(languageText);
			
			NodeList nodeList = new NodeList();
			nodeList.add(textNode);
			linkTag.setChildren(nodeList);
			
			linkTag.setLink(url);
			languageLinksList.add(new TextNode("\t\t"));
			languageLinksList.add(linkTag);
			languageLinksList.add(new TextNode("</a>"));
			languageLinksList.add(new TextNode(linkSeparator));
		} else{
			existingLinkTag.setLink(url);
		}
	}	
}