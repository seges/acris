package com.google.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by PeterSimun on 20.4.2014.
 */
public class ClientRect extends JavaScriptObject {

    protected ClientRect() {}

    /**
     * @return The distance of the element to the bottom of the viewport.
     */
    public final native double getBottom() /*-{
        return this.bottom;
    }-*/;

    /**
     * @return The height of the element.
     */
    public final native double getHeight() /*-{
        return this.height;
    }-*/;

    /**
     * @return The distance of the element to the left of the viewport.
     */
    public final native double getLeft() /*-{
        return this.left;
    }-*/;

    /**
     * @return The distance of the element to the right of the viewport.
     */
    public final native double getRight() /*-{
        return this.right;
    }-*/;

    /**
     * @return The distance of the element to the top of the viewport.
     */
    public final native double getTop() /*-{
        return this.top;
    }-*/;

    /**
     * @return The width of the element.
     */
    public final native double getWidth() /*-{
        return this.width;
    }-*/;
}