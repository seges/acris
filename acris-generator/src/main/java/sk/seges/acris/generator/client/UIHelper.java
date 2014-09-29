package sk.seges.acris.generator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

@SuppressWarnings("deprecation")
public class UIHelper {

	private static native String getNodeName(Element elem) /*-{
		return (elem.nodeName || "").toLowerCase();
	}-*/;

	public static void cleanUI() {
		
		Element bodyElem = RootPanel.getBodyElement();

		List<Element> toRemove = new ArrayList<Element>();
		for (int i = 0, n = DOM.getChildCount(bodyElem); i < n; ++i) {
			Element elem = DOM.getChild(bodyElem, i);
			String nodeName = getNodeName(elem);
			if (!"script".equals(nodeName) && !"iframe".equals(nodeName)) {
				toRemove.add(elem);
			}
		}

		for (int i = 0, n = toRemove.size(); i < n; ++i) {
			DOM.removeChild(bodyElem, toRemove.get(i));
		}
		
		//RootPanel.get().clear();
	}
}