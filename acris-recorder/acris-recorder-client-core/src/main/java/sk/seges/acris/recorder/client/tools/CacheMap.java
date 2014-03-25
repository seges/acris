package sk.seges.acris.recorder.client.tools;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap extends LinkedHashMap<String, Element> {

	private static final long serialVersionUID = -3807095271203050282L;

	private int maxCapacity;

	public CacheMap(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	protected boolean removeEldestEntry(Map.Entry<String, Element> eldest) {
		return size() > maxCapacity;
	}

	private static final native Element getByXpath(String path) /*-{
        return $doc.evaluate(path, $doc, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
    }-*/;

	public Element resolveElement(String xpath) {
		Element el = get(xpath);

		if (el != null) {
			return el;
		}

		if (xpath != null && xpath.length() > 0) {
			el = getByXpath(xpath);
		} else {
			el = Document.get().getDocumentElement().cast();
		}

		put(xpath, el);

		return el;
	}
}
