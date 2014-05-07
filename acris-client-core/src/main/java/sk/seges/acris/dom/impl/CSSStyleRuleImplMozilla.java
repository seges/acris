package sk.seges.acris.dom.impl;

import sk.seges.acris.dom.CSSStyleRule;
import sk.seges.acris.dom.StyleSheet;

public class CSSStyleRuleImplMozilla extends CSSStyleRuleImpl {

	public native CSSStyleRule getStyleSheetRule(StyleSheet styleSheet, int index) /*-{
		return styleSheet.cssRules[index];
	}-*/;

	public native int getStyleSheetRulesCount(StyleSheet styleSheet) /*-{
		return styleSheet.cssRules.length;
	}-*/;
}