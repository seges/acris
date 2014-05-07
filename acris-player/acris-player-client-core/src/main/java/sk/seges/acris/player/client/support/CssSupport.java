package sk.seges.acris.player.client.support;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import sk.seges.acris.dom.CSSStyleRule;
import sk.seges.acris.dom.StyleSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PeterSimun on 6.5.2014.
 */
public class CssSupport {

    //TODO caching should be configurable, for synapso it can be changed
    private SelectorRules selectorRules = new SelectorRules();
    private ElementCSSSelectorCache elementCSSSelectorCache = new ElementCSSSelectorCache();
    private ElementStyleCache elementStyleCache = new ElementStyleCache();

    private class ElementCSSSelectorCache {

        private Map<String, Element[]> elementsCache = new HashMap<String, Element[]>();

        public Element[] getElements(String selectorName) {
            Element[] elements = elementsCache.get(selectorName);

            if (elements == null) {
                GQuery el = GQuery.$(selectorName);
                elements = el.elements();
                if (elements == null) {
                    elements = new Element[]{};
                }
                elementsCache.put(selectorName, elements);
            }

            return elements;
        }
    }

    private class SelectorRules {

        private Map<String, List<CSSStyleRule>> selectorsCache = new HashMap<String, List<CSSStyleRule>>();

        private void addRules(String selector, CSSStyleRule rule) {
            List<CSSStyleRule> cssStyleRules = selectorsCache.get(selector);
            if (cssStyleRules == null) {
                cssStyleRules = new ArrayList<CSSStyleRule>();
                selectorsCache.put(selector, cssStyleRules);
            }
            cssStyleRules.add(rule);
        }

        private List<CSSStyleRule> getRules(String selector) {
            return selectorsCache.get(selector);
        }

        private void setRules(String selector, List<CSSStyleRule> rules) {
            selectorsCache.put(selector, rules);
        }

        public List<CSSStyleRule> getRulesFor(String pseudoClass) {
            List<CSSStyleRule> result = getRules(pseudoClass);

            if (result != null) {
                return result;
            }

            result = new ArrayList<CSSStyleRule>();
            selectorRules.setRules(pseudoClass, result);

            int count = StyleSheet.getStylesheetsCount();

            for (int i = 0; i < count; i++) {
                StyleSheet stylesheet = StyleSheet.getStylesheet(i);
                int rulesCount = stylesheet.getRuleCount();

                for (int k = 0; k < rulesCount; k++) {
                    CSSStyleRule rule = stylesheet.getRule(k);
                    String selectorText = rule.getSelectorText();
                    if (selectorText != null) {
                        String[] selectors = selectorText.split(",");

                        for (String selector: selectors) {
                            if (selector.toLowerCase().contains(":" + pseudoClass.toLowerCase())) {
                                result.add(rule);
                            }
                        }
                    }
                }
            }

            return result;
        }

    }

    private class ElementStyleCache {

        private Map<Element, Map<String, String>> styleCache = new HashMap<Element, Map<String, String>>();

        public void rememberStyle(Element element, String property, String value) {
             Map<String, String> properties = styleCache.get(element);

            if (properties == null) {
                properties = new HashMap<String, String>();
                styleCache.put(element, properties);
            }

            properties.put(property, value);
        }

        public Map<String, String> getElementOriginalStyles(Element element) {
            return styleCache.get(element);
        }
    }

    public enum PseudoClassType {
        HOVER("hover");

        private final String name;

        private PseudoClassType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public CssSupport() {}

    public void applyPseudoClassStyle(Element element, PseudoClassType pseudoClassType) {

        List<CSSStyleRule> rules = selectorRules.getRulesFor(pseudoClassType.getName());

        for (CSSStyleRule rule: rules) {

            String selectorText = rule.getSelectorText();

            if (match(element, selectorText, pseudoClassType.getName())) {
                JsArrayString properties = getProperties(rule.getStyle());

                int length = properties.length();

                for (int p = 0; p < length; p++) {
                    String property = toCamelCase(properties.get(p));
                    elementStyleCache.rememberStyle(element, property, element.getStyle().getProperty(property));
                    element.getStyle().setProperty(property, rule.getStyle().getProperty(property));
                }
            }
        }
    }

    public void restoreStyles(Element element) {
        Map<String, String> elementOriginalStyles = elementStyleCache.getElementOriginalStyles(element);

        if (elementOriginalStyles == null) {
            return;
        }

        for (Map.Entry<String, String> styleEntry: elementOriginalStyles.entrySet()) {
            element.getStyle().setProperty(styleEntry.getKey(), styleEntry.getValue());
        }
    }

    private static String toCamelCase(String s){
        String[] parts = s.split("-");
        String camelCaseString = parts[0];
        for (int i = 1; i < parts.length; i++){
            camelCaseString = camelCaseString + toProperCase(parts[i]);
        }
        return camelCaseString;
    }

    private static String toProperCase(String s) {
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }
        return s.toUpperCase();
    }


    private boolean match(Element element, String selectorText, String pseudoClass) {
        //not only contains, but split by commas and match exactly
        String[] selectors = selectorText.split(",");
        for (String selector: selectors) {

            String selectorName = selector.toLowerCase().replace(":" + pseudoClass.toLowerCase(), "");

            Element[] elements = elementCSSSelectorCache.getElements(selectorName);

            for (Element e: elements) {
                if (e.equals(element)) {
                    return true;
                }
            }
        }

        return false;
    }

    private native JsArrayString getProperties(JavaScriptObject jso) /*-{
        var result = {}
        for (att in jso)
            result[att] = jso[att];
        return result;
    }-*/;

}
