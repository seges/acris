package sk.seges.acris.dom.impl;

import sk.seges.acris.dom.CSSStyleRule;
import sk.seges.acris.dom.StyleSheet;

public class CSSStyleRuleImplIE6 extends CSSStyleRuleImpl {

	public native CSSStyleRule getStyleSheetRule(StyleSheet styleSheet, int index) /*-{
		return styleSheet.rules[index];
	}-*/;
	
	public native int getStyleSheetRulesCount(StyleSheet styleSheet) /*-{
		return styleSheet.rules.length;
	}-*/;

}
