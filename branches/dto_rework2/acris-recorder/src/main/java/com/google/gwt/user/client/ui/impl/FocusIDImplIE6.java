package com.google.gwt.user.client.ui.impl;

import com.google.gwt.user.client.Element;

public class FocusIDImplIE6 extends FocusImplIE6 {
	public native Element createFocusable() /*-{
	    var e = $doc.createElement("DIV");
	    e.tabIndex = 0;
	    @sk.seges.acris.client.utils.DOMUtil::generateId(Lcom/google/gwt/dom/client/Element;)(e);
	    return e;
	}-*/;
}
