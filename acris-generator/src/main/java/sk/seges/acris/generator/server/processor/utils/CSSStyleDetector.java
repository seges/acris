package sk.seges.acris.generator.server.processor.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Tag;


public class CSSStyleDetector {

	private static Log log = LogFactory.getLog(CSSStyleDetector.class);

	private static final String STYLE_TAG_NAME = "style";

	private Map<String, String> tagStyles = new HashMap<String, String>();
	
	//merge with edit style support
	public CSSStyleDetector(Tag tag) {
		String styleValue = tag.getAttribute(STYLE_TAG_NAME);
		if (styleValue != null) {
			
			String[] styles = styleValue.split(";");
			for (String style: styles) {
				int index = style.indexOf(":");
				if (index != -1) {
					String key = style.substring(0, index);
					String value = style.substring(index + 1);
					tagStyles.put(key.trim().toLowerCase(), value.trim().toLowerCase());
				} else {
					log.warn("Invalid style value '" + style + "' in the tag " + tag.getTagName());
				}
			}
		}
	}

	public boolean hasStyle(String key) {
		return tagStyles.get(key.toLowerCase()) != null;
	}
	
	public String getStyleValue(String key) {
		return tagStyles.get(key.toLowerCase());
	}
	
	private static final String VISIBILITY = "visibility";
	private static final String DISPLAY = "display";
	
	public boolean isVisible() {
		if (!hasStyle(VISIBILITY) &&
			!hasStyle(DISPLAY)) {
			return true;
		}

		if (!hasStyle(DISPLAY)) {
			return !("hidden".equals(getStyleValue(VISIBILITY)));
		}

		if (!hasStyle(VISIBILITY)) {
			return !("none".equals(getStyleValue(DISPLAY)));
		}
		
		return !("hidden".equals(getStyleValue(VISIBILITY))) && 
		 	   !("none".equals(getStyleValue(DISPLAY)));
	}
}
