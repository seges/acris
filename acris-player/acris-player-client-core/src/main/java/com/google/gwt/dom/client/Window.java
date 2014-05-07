package com.google.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by PeterSimun on 21.4.2014.
 */
public class Window extends JavaScriptObject {

    protected Window() {}

    public static final native Window defaultView() /*-{
        return $doc.defaultView;
    }-*/;
}
