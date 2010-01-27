package sk.seges.acris.generator.server.processor.post.alters;

import java.io.File;

import org.htmlparser.Node;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;

@Component
public class StylesPathPostProcessor extends AbstractContentInfoPostProcessor {

	private static final String STYLESHEET_REL = "stylesheet";
	private static final String CSS_TYPE = "text/css";
	
	@Override
	public boolean supports(Node node) {
		
		if (node instanceof StyleLinkTag) {
			StyleLinkTag styleLinkTag = (StyleLinkTag)node;
			return (compareIgnoreCaseNullSafe(styleLinkTag.getRel(), STYLESHEET_REL) &&
					compareIgnoreCaseNullSafe(styleLinkTag.getType(), CSS_TYPE));
		}

		return false;
	}

	private boolean compareIgnoreCaseNullSafe(String text1, String text2) {
		if (text1 == null || text2 == null) {
			return false;
		}
		
		return (text1.trim().toLowerCase().equals(text2.trim().toLowerCase()));
	}
	
	@Override
	public boolean process(Node node) {
		String niceUrl = generatorToken.getNiceUrl(); 
		if (File.separatorChar != '/')
			niceUrl = niceUrl.replace(File.separatorChar, '/');

		int count = niceUrl.split("/").length;
		
		if (count <= 1) {
			return true; //no special processing necessary 
		}

		StyleLinkTag styleLinkTag = (StyleLinkTag)node;
		
		String pathPrefix = "";
		
		for (int i = 1; i < count; i++) {
			pathPrefix = pathPrefix + ".." + File.separator;
		}
		
		styleLinkTag.setHref(pathPrefix + styleLinkTag.getHref());
		
		return true;
	}
}
