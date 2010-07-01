package com.google.gwt.dom.client;

import com.google.gwt.user.client.Element;

public class DOMIDImplIE6 extends DOMImplIE6 {
	public native Element createElement(Document doc, String tag) /*-{
	    var e = doc.createElement(tag);
        @sk.seges.acris.client.utils.DOMUtil::generateId(Lcom/google/gwt/dom/client/Element;)(e);
	  	return e;
	}-*/;
	
	public native InputElement createInputElement(Document doc, String type) /*-{
	    var e = doc.createElement("INPUT");
	  	e.type = type;
        @sk.seges.acris.client.utils.DOMUtil::generateId(Lcom/google/gwt/dom/client/Element;)(e);
		return e;
	}-*/;
}
