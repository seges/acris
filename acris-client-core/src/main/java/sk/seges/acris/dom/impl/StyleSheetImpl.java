package sk.seges.acris.dom.impl;

import sk.seges.acris.dom.StyleSheet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class StyleSheetImpl {

	static final CSSStyleRuleImpl impl = GWT.create(CSSStyleRuleImpl.class);

	public native StyleSheet getStylesheet(int index) /*-{
		return $doc.styleSheets[index];
	}-*/;

	public native int getStylesheetsCount() /*-{
		return $doc.styleSheets.length;
	}-*/;

	public native String getStyleValue(Element element, String prop) /*-{
	
		if (element) {
			if ($wnd.getComputedStyle) {
				return $wnd.getComputedStyle(element,'').getPropertyValue(prop.replace(/([A-Z])/g,"-$1").toLowerCase());
			} else if (element.currentStyle) {
				return element.currentStyle[prop];
			}

			return null;
		}
		return null;
	}-*/;

	public CSSStyleRuleImpl getCSSStyleRuleImpl() {
		return impl;
	}
}