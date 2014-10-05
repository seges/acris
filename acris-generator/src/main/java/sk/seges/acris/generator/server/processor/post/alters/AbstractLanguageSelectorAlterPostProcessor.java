package sk.seges.acris.generator.server.processor.post.alters;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;

import java.util.List;

public abstract class AbstractLanguageSelectorAlterPostProcessor<T extends Tag, Child extends Tag> extends AbstractAlterPostProcessor {

	private static final String LANGUAGE_SELECTOR_STYLE_CLASS_NAME = "acris-language-selector-panel";
	protected static final String STYLE_ATTRIBUTE_NAME = "style";
	protected static final String LOCALE_ATTRIBUTE_NAME = "locale";
		
	protected String LANGUAGE_BAR_STYLE = "height: 32px; overflow-y: scroll";
	
    private static final Logger LOG = Logger.getLogger(AbstractLanguageSelectorAlterPostProcessor.class);
    
    private ContentDataProvider contentDataProvider;
    
    public AbstractLanguageSelectorAlterPostProcessor(ContentDataProvider contentDataProvider) {
    	this.contentDataProvider = contentDataProvider;
    }
    
    protected abstract Class<T> getLanguageSelectorTagClass();
    
    @SuppressWarnings("unchecked")
	protected Class<Child> getLanguageSelectorChildTagClass() {
    	return ((Class<Child>) getLanguageSelectorTagClass());
    }
    
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TagNode) {
			String classAttributeValue = ((TagNode)node).getAttribute(CLASS_ATTRIBUTE_NAME);
			
			if (classAttributeValue != null && classAttributeValue.length() > 0) {
				String[] styleClasses = classAttributeValue.split(" ");
				
				for (String styleClass : styleClasses) {
					if (styleClass.trim().equals(LANGUAGE_SELECTOR_STYLE_CLASS_NAME)) {
						return hasChildNode(node, getLanguageSelectorTagClass());
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		return processTag(iterateToNode(node, getLanguageSelectorTagClass()), (TagNode) node, generatorEnvironment);
	}

	protected LinkTag createAnchorNode(String text, String url) {
		
		LinkTag linkTag = new LinkTag();
		
		if (text != null) {
			TextNode textNode = new TextNode(text);
			
			NodeList nodeList = new NodeList();
			nodeList.add(textNode);
			linkTag.setChildren(nodeList);
		}
		
		linkTag.setLink(url);
		LinkTag endTag = new LinkTag();
		endTag.setTagName("/" + endTag.getTagName());
		linkTag.setEndTag(endTag);

		return linkTag;
	}
	
	protected boolean processTag(T tag, TagNode parentNode, GeneratorEnvironment generatorEnvironment) {
		
		NodeList languageLinksList = new NodeList();
		
		NodeList children = getLanguageNodes(tag, parentNode);//tag.getParent().getChildren();
		
		for (int i = 0; i < children.size(); i++){
			Node childNode = children.elementAt(i);
			
			if (childNode.getClass().equals(getLanguageSelectorChildTagClass())) {
				
				@SuppressWarnings("unchecked")
				Child nestedTag = (Child)childNode;
				
				String targetUrl = getTargetUrl(generatorEnvironment, getLanguageShort(nestedTag));
				
				if (targetUrl != null) {
					List<Node> nodes = processLocaleTag(targetUrl, getLanguageName(nestedTag), nestedTag, parentNode);
					for (Node localeNode: nodes) {
						languageLinksList.add(localeNode);
					}
				}
			} else {
				languageLinksList.add(childNode);
			}
		}
		
		parentNode.setChildren(languageLinksList);

		return true;
	}
	
	protected abstract List<Node> processLocaleTag(String url, String text, Child currentTag, Node parentNode);
	
	protected NodeList getLanguageNodes(T tag, TagNode parentNode) {
		return tag.getParent().getChildren();
	}
	
	protected abstract String getLanguageName(Child tag);
	
	protected String getLanguageShort(Child tag) {
		return tag.getAttribute(LOCALE_ATTRIBUTE_NAME);
	}

	protected String getTargetUrl(GeneratorEnvironment generatorEnvironment, String locale) {

		if (locale == generatorEnvironment.getGeneratorToken().getLanguage()) {
			return null;
		}
		
		WebSettingsData linkWebSettings = generatorEnvironment.getWebSettings();
		
		if (generatorEnvironment.getContent() == null) {
			return null;
		}
		
		LOG.debug("Creating link for language: " + locale + " and content (" + generatorEnvironment.getContent() + ") with nice url: " + 
					generatorEnvironment.getContent().getNiceUrl());
		ContentData content = contentDataProvider.getContentForLanguage(generatorEnvironment.getContent(), locale);
		
		if (content == null) {			
			content = contentDataProvider.getHomeContent(locale, generatorEnvironment.getContent().getWebId());
			
			if (content == null) {
				LOG.error("No home page found for webId: " + generatorEnvironment.getContent().getWebId() + " and language: " + locale);
				return null;
			}
		}

		LOG.debug("Found content for language: " + locale + " with nice url: " + content.getNiceUrl());
		
		String translatedNiceUrl = content.getNiceUrl();
		String url = linkWebSettings.getTopLevelDomain();
		
		if (url == null) {
			LOG.warn("Web " + generatorEnvironment.getGeneratorToken().getWebId() + " doesn't have set top level domain. Using empty URL!");
			url = "";
		}
		
		if (translatedNiceUrl != null) {
			if (!url.endsWith("/")) {
				url += "/";
			}
			url += translatedNiceUrl;
		}
		
		return url;
	}
}