package sk.seges.acris.widget.client;

import com.google.gwt.user.client.Element;

/**
 * Extends standard GWT hyperlink to allow to wrap existing DOM elements into
 * it.
 * 
 * @author ladislav.gazo
 */
public class Hyperlink extends com.google.gwt.user.client.ui.Hyperlink {
	protected Hyperlink(Element elem) {
		super(elem);
	}

	public static Hyperlink wrap(com.google.gwt.dom.client.Element child) {
		return new Hyperlink((Element) child);
	}
}
