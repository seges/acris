package sk.seges.acris.dom.impl;

import sk.seges.acris.dom.CSSStyleRule;
import sk.seges.acris.dom.StyleSheet;

public abstract class CSSStyleRuleImpl {
	public abstract CSSStyleRule getStyleSheetRule(StyleSheet styleSheet, int index);

	public abstract int getStyleSheetRulesCount(StyleSheet styleSheet);

	public CSSStyleRule getStyleSheetRule(StyleSheet styleSheet, String selector) {

		int count = getStyleSheetRulesCount(styleSheet);

		for (int i = 0; i < count; i++) {
			CSSStyleRule csstyleRule = getStyleSheetRule(styleSheet, i);
			if (csstyleRule != null && selector.equals(csstyleRule.getSelectorText())) {
				return csstyleRule;
			}
		}

		return null;
	}
}
