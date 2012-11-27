package com.google.gwt.user.client.impl;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;

public class DOMIDImplIE extends DOMImplIE8 {

	@Override
	public native void setEventListener(Element elem, EventListener listener) /*-{
	    elem.__listener = listener;
	    @sk.seges.acris.recorder.client.utils.DOMUtil::generateId(Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/user/client/EventListener;)(elem, listener);
	}-*/;

}
