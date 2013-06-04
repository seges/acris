package sk.seges.acris.generator.server.processor.utils;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Tag;

public class CSSStyleClassDetector {

	private static final String STYLE_CLASS_TAG_NAME = "class";
	
	private final List<String> classes = new ArrayList<String>();
	
	public CSSStyleClassDetector(Tag tag) {
		String styleClassValue = tag.getAttribute(STYLE_CLASS_TAG_NAME);
		if (styleClassValue != null) {
			for (String styleClass: styleClassValue.split(" ")) {
				styleClass = prepareStyleClass(styleClass);
				
				if (styleClass != null) {
					classes.add(styleClass);
				}
			}
		}
	}
	
	private String prepareStyleClass(String styleClass) {
		if (styleClass == null) {
			return null;
		}
		return styleClass.trim().toLowerCase();
	}
	
	public boolean hasStyleClass(String styleClass) {
		return classes.contains(prepareStyleClass(styleClass));
	}
}