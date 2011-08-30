/**
 * 
 */
package sk.seges.acris.widget.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Cleaner element representing div with cleaner style (acris-cmp-cleaner).
 * 
 * @author ladislav.gazo
 */
public class Cleaner extends Composite {
	public static final String CLEANER_CLASS = "acris-cleaner";
	public static final String ATTR_CLEANER = "cleaner";
	
	public Cleaner() {
		SimplePanel panel = new SimplePanel();
		panel.setStyleName(CLEANER_CLASS);
		createCleanerAttributes(panel.getElement());
		initWidget(panel);
	}
	
	public static Element createElement() {
		Element element = DOM.createDiv();
		createCleanerAttributes(element);
		return element;
	}
	
	public static boolean is(Element element) {
		return "true".equalsIgnoreCase(element.getAttribute(ATTR_CLEANER));
	}

	private static void createCleanerAttributes(Element element) {
		element.setClassName(CLEANER_CLASS);
		element.setAttribute(ATTR_CLEANER, "true");
	}
}
