package sk.seges.acris.recorder.rpc.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;

public class CacheMap extends LinkedHashMap<String, Element> {

	private static final long serialVersionUID = -3807095271203050282L;

	private int maxCapacity;

	public CacheMap(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	protected boolean removeEldestEntry(Map.Entry<String, Element> eldest) {
		return size() > maxCapacity;
	}

	public Element resolveElement(String elementId) {
		Element el = get(elementId);

		if (el != null) {
			return el;
		}

		if (elementId != null && elementId.length() > 0) {
//			GQuery gquery = $("*[elementID="+elementId+"]");
//			el = gquery.get(0).<com.google.gwt.user.client.Element>cast();
		} else {
			el = Document.get().getDocumentElement().<com.google.gwt.user.client.Element>cast();
		}

		put(elementId, el);

		return el;
	}
}
