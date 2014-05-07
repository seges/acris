package sk.seges.acris.dom;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;

public class CSSStyleRule extends JavaScriptObject {

	protected CSSStyleRule() {	
	}
	
	public final native Style getStyle() /*-{
		return this.style;
	}-*/;

	public final native String getCssText() /*-{
		return this.style.cssText;
	}-*/;
	
	public final native String getSelectorText() /*-{
		return this.selectorText;
	}-*/;
}