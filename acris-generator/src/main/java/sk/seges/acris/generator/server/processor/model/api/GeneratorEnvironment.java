package sk.seges.acris.generator.server.processor.model.api;

import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;

import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public interface GeneratorEnvironment {

	GeneratorToken getGeneratorToken();
	GeneratorToken getDefaultToken();
	
	ContentData getContent();
	
	WebSettingsData getWebSettings();
		
	boolean isIndexFile();
	
	public interface NodesContext {
		HeadTag getHeadNode();
		
		void setHeadNode(HeadTag headNode);
		
		Html getHtmlNode();
		
		void setHtmlNode(Html htmlNode);
	}
	
	public NodesContext getNodesContext();
}