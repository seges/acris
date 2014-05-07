package sk.seges.acris.dom;

import sk.seges.acris.dom.impl.StyleSheetImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class StyleSheet extends JavaScriptObject {
	
	static final StyleSheetImpl impl = GWT.create(StyleSheetImpl.class);
	static final String VERSION_PARAM = "?ver";
	
	protected StyleSheet() {}

    public final static int getStylesheetsCount() {
        return impl.getStylesheetsCount();
    };

    public final static StyleSheet getStylesheet(int index) {
		return impl.getStylesheet(index);
	};

	public final static String getStyleValue(Element element, String property) {
		return impl.getStyleValue(element, property);
	}
	
	public final static StyleSheet getStylesheet(String name) {

		if (!name.toLowerCase().endsWith(".css")) {
			name += ".css";
		}
		
		int count = impl.getStylesheetsCount();
		
		for (int i = 0; i < count; i++) {
			StyleSheet styleSheet = impl.getStylesheet(i);
			if (styleSheet == null) {
				break;
			}
			String href = styleSheet.getHref();
			if (href != null && href.contains(VERSION_PARAM)) {
				href = href.substring(0, href.lastIndexOf(VERSION_PARAM));
			}
			if (href != null && href.endsWith(name)) {
				return styleSheet;
			}
		}
		
		return null;
	};

	public final native String getHref() /*-{
		return this.href;
	}-*/;
		  
	public final native boolean isDisabled() /*-{
		return this.disabled;
	}-*/;
	
	public final native String getType() /*-{
		return this.type;
	}-*/;
	
	public final native String getTitle() /*-{
		return this.title;
	}-*/;
	
	public final int getRuleCount() {
		return impl.getCSSStyleRuleImpl().getStyleSheetRulesCount(this);
	}
	
	public final CSSStyleRule getRule(int index) {
		return impl.getCSSStyleRuleImpl() != null ? impl.getCSSStyleRuleImpl().getStyleSheetRule(this, index) : null;
	};

	public final CSSStyleRule getRule(String name) {
		return impl.getCSSStyleRuleImpl() != null ? impl.getCSSStyleRuleImpl().getStyleSheetRule(this, name) : null;
	};
}
