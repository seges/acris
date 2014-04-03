package sk.seges.acris.recorder.client.tools;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap {

	private static final long serialVersionUID = -3807095271203050282L;

	private int maxCapacity;

    private LinkedHashMap<String, Element> xpathsMap;
    private LinkedHashMap<Element, String> elementsMap;

	public CacheMap(int maxCapacity) {
		this.maxCapacity = maxCapacity;

        xpathsMap = new LinkedHashMap<String, Element>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Element> eldest) {
                return size() > CacheMap.this.maxCapacity;
            }
        };

        elementsMap = new LinkedHashMap<Element, String>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Element, String> eldest) {
                return size() > CacheMap.this.maxCapacity;
            }
        };
	}

	private static final native Element getByXpath(String path) /*-{
        return $doc.evaluate(path, $doc, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
    }-*/;

    public String resolveXpath(Element element) {
        String xpath = elementsMap.get(element);

        if (xpath == null) {
            xpath = getXPath(element);
        }

        elementsMap.put(element, xpath);
        xpathsMap.put(xpath, element);

        return xpath;
    }

    protected final boolean containsMoreDigits(String s){

        if (s == null || s.isEmpty()) {
            return false;
        }

        int digitCount = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
                if (digitCount > 3) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasId(com.google.gwt.dom.client.Element element) {
        //when it contains more that 4 digits its probably generated
        return (element != null && element.getId() != null && element.getId().length() > 0 && !containsMoreDigits(element.getId()));
    }

    protected String getXPathForId(com.google.gwt.dom.client.Element element) {
        return "//*[@id='" + element.getId() + "']";
    }

    protected String getXPath(com.google.gwt.dom.client.Element element) {
        if (hasId(element)) {
            return getXPathForId(element);
        }
        return getElementTreeXpath(element);
    }

    protected String getElementTreeXpath(com.google.gwt.dom.client.Element element) {
        String result = "";

        // Use nodeName (instead of localName) so namespace prefix is included (if any).
        for (; element != null && element.getNodeType() == 1; element = element.getParentElement()) {

            if (result.length() > 0) {
                result = "/" + result;
            }

            if (hasId(element)) {
                return getXPathForId(element) + result;
            }

            int index = 0;
            for (Node sibling = element.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling())
            {
                // Ignore document type declaration.
                if (sibling.getNodeType() == Node.DOCUMENT_NODE)
                    continue;

                if (sibling.getNodeName().equals(element.getNodeName())) {
                    ++index;
                }
            }

            String tagName = element.getNodeName().toLowerCase();
            String pathIndex = (index > 0 ? "[" + (index+1) + "]" : "");

            result = tagName + pathIndex + result;
        }

        return result;
    }

    public Element resolveElement(String xpath) {
		Element el = xpathsMap.get(xpath);

		if (el != null) {
			return el;
		}

		if (xpath != null && xpath.length() > 0) {
			el = getByXpath(xpath);
		} else {
			el = Document.get().getDocumentElement().cast();
		}

        xpathsMap.put(xpath, el);
        elementsMap.put(el, xpath);

		return el;
	}
}