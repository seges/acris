package sk.seges.acris.generator.server.processor.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Tag;

import java.util.*;


public class CSSStyleDetector {

	private static Log log = LogFactory.getLog(CSSStyleDetector.class);

	private static final String STYLE_ATTRIBUTE_NAME = "style";
	private static final String CLASS_ATTRIBUTE_NAME = "class";

	private Map<String, String> tagStyles = new HashMap<String, String>();
	private List<String> tagClasses = new ArrayList<String>();
	
	//merge with edit style support
	public CSSStyleDetector(Tag tag) {
		String styleValue = tag.getAttribute(STYLE_ATTRIBUTE_NAME);
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
		
		String styleClassValue = tag.getAttribute(CLASS_ATTRIBUTE_NAME);
		
		if (styleClassValue != null) {
			tagClasses.addAll(Arrays.asList(styleClassValue.trim().split(" ")));
		}
	}

	public boolean hasStyle(String key) {
		return tagStyles.get(key.toLowerCase()) != null;
	}
	
	public String getStyleValue(String key) {
		String result = tagStyles.get(key.toLowerCase());
		if (result == null) {
			return null;
		}		
		return result.toLowerCase().trim();
	}
	
	private static final String VISIBILITY = "visibility";
	private static final String DISPLAY = "display";
	
	private static String[] skipElementsClasses = new String[] {
		"chzn-select"
	};
	
	public boolean isVisible() {
		
		for (String skipElementClass: skipElementsClasses) {
			if (tagClasses.contains(skipElementClass)) {
				return true;
			}
		}
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
